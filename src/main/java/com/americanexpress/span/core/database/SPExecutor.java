/**
 * Copyright 2020 American Express Travel Related Services Company, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.americanexpress.span.core.database;

import com.americanexpress.span.core.SPANConfigHolder;
import com.americanexpress.span.core.database.connection.SPANDataSource;
import com.americanexpress.span.core.database.handlers.InputHandler;
import com.americanexpress.span.core.database.handlers.OutputHandler;
import com.americanexpress.span.core.database.handlers.ResultSetHandler;
import com.americanexpress.span.core.database.handlers.impl.InputHandlerImpl;
import com.americanexpress.span.core.database.handlers.impl.OutputHandlerImpl;
import com.americanexpress.span.core.database.handlers.impl.ResultSetHandlerImpl;
import com.americanexpress.span.exceptions.DuplicateFieldException;
import com.americanexpress.span.exceptions.SPANException;
import com.americanexpress.span.models.SPUserDefineKey;
import com.americanexpress.span.utility.SPANUtility;
import com.americanexpress.span.utility.ThreadContext;
import com.google.common.base.Strings;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * SPExecutor- Will do following things
 * 1. Transform the Input Field and assign to SP InputParam
 * 2. Execute SP
 * 2. Transform Output param
 */
public class SPExecutor {

    public static final String MDC_SQL_QUERY = "SQL_Query";
    public static final String MDC_FIELD = "Field";
    private static final String CALL_STATEMENT = "{call %NAME%(%PARAM_LIST%)}";
    private ResultSetHandler resultSetHandler;

    private InputHandler inputHandler;

    private OutputHandler outputHandler;


    public SPExecutor(final InputHandler inputHandler, final OutputHandler outputHandler, final ResultSetHandler resultSetHandler) {
        this.inputHandler = inputHandler;
        this.outputHandler = outputHandler;
        this.resultSetHandler = resultSetHandler;
    }

    public SPExecutor() {
    }

    /**
     * This method validates the fields and check for duplicate field names for InParams, OutParams and ResultSet.
     *
     * @param clazz
     */
    public static void validateFields(Class clazz) {

        Set<String> unique = new HashSet<>();
        SPANUtility.getAnnotatedDeclaredFields(clazz).forEach(field -> {
            if (unique.add(field.getAnnotation(com.americanexpress.span.annotation.Field.class).name()) == Boolean.FALSE) {
                throw new DuplicateFieldException("Duplicate fields in " + clazz.getSimpleName());
            }
        });
        SPANUtility.getAnnotatedDeclaredMethods(clazz).forEach(method -> {
            if (null != method.getAnnotation(com.americanexpress.span.annotation.Field.class)
                    && (unique.add(method.getAnnotation(com.americanexpress.span.annotation.Field.class).name()) == Boolean.FALSE)) {
                    throw new DuplicateFieldException("Duplicate fields in " + clazz.getSimpleName());
            }

        });
        SPANUtility.getAnnotatedDeclaredFields(clazz, com.americanexpress.span.annotation.ResultSet.class).forEach(resultSet -> {
            ParameterizedType type = (ParameterizedType) resultSet.getGenericType();
            Class<?> clazzType = (Class<?>) type.getActualTypeArguments()[0];
            //validations for Duplicate Fields in ResultSet class
            validateFields(clazzType);
        });
    }

    public void setInputHandler(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
    }

    public void setOutputHandler(OutputHandler outputHandler) {
        this.outputHandler = outputHandler;
    }

    public void setResultSetHandler(ResultSetHandler resultSetHandler) {
        this.resultSetHandler = resultSetHandler;
    }

    public <I, O> O execute(final String spUserDefineKey, final I spInput, final Class<O> returnType) throws SQLException {
        //track with key name
        ThreadContext.set("SPAN-SP-KEY", spUserDefineKey);
        checkAndSetDefaultImpls();

        assert spInput != null && returnType != null && !Strings.isNullOrEmpty(spUserDefineKey) : "SPUser Defined Key: " + spUserDefineKey
                + ". Input Object: " + spInput + ". ReturnType: " + returnType + " cannot be null.";

        //Validates Inparam Class Fields
        validateFields(spInput.getClass());
        //Validates OutParam Class Fields
        validateFields(returnType);


        DataSource dataSource = SPANDataSource.getInstance().getDataSource(spUserDefineKey);

        assert dataSource != null : "Invalid SPUserDefined Key. Datasource cannot be null. " + spUserDefineKey;

        Object spOutput = null;
        try (final Connection conn = dataSource.getConnection()) {
            if (conn == null) {
                throw new IllegalStateException("DataSource returned null from getConnection(): " + dataSource);
            }
            String spQuery = getSPQuery(spUserDefineKey, spInput.getClass(), returnType);
            ThreadContext.set(MDC_SQL_QUERY, spQuery);
            LoggerFactory.getLogger(SPExecutor.class).warn("Executing SQL Query. " + ThreadContext.get(MDC_SQL_QUERY));

            try (CallableStatement stmt = conn.prepareCall(spQuery)) {
                inputHandler.processSPInputParameters(stmt, spInput);
                outputHandler.registerOutputParameters(stmt, returnType);

                //moreresults - will true if resultset is available otherwise it will be false but accordingly javadocs
                //updatecount should also check, if it is 0 then it may have resultset.
                boolean moreResults = stmt.execute();
                int updateCount = stmt.getUpdateCount();

                spOutput = outputHandler.processSPOutputParameters(stmt, returnType);


                Map<Integer, Field> seqToField = getMappingFromSeqToField(SPANUtility.getUniqueAnnotationFieldsFromClass(returnType, com.americanexpress.span.annotation.ResultSet.class));

                if (moreResults == false  && updateCount == -1 && !seqToField.isEmpty()) {
                    Field field = seqToField.get(1);
                    try {
                        field.setAccessible(true);
                        field.set(spOutput, Collections.emptyList());
                    } catch (IllegalAccessException e) {
                        throw new SPANException("Unable to set ResultSet for the object in Class: " + returnType
                                + ". " + ThreadContext.get(MDC_SQL_QUERY));
                    }
                } else {

                    int seqNum = 1;
                    int totalResultSets = seqToField.size();
                    // Loop will process each resultSet and remove the sequence entry from seqToField.
                    //accordingly java docs.
                    //getMoreResults is moving cursor and if it has resultset then its return true.
                    //so by default it has one resultset is open.
                    while (moreResults || updateCount != -1) {
                        try (ResultSet rs = stmt.getResultSet()) {
                            if (rs != null) {
                                Field field = seqToField.get(seqNum);
                                if (field == null) {
                                    LoggerFactory.getLogger(SPExecutor.class).warn("ResultSets SeqNum: is ignored from the Stored Procedure: {}", seqNum, ThreadContext.get(MDC_SQL_QUERY));
                                    continue;
                                }
                                if (field.getType() != List.class) {
                                    throw new SPANException("ResultSet Field should be a List.");
                                }
                                List resultSetObjs = resultSetHandler.processResultSet(rs, (Class) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0]);
                                if (resultSetObjs != null) {
                                    field.setAccessible(true);
                                    try {
                                        field.set(spOutput, resultSetObjs);
                                    } catch (IllegalAccessException e) {
                                        throw new SPANException("Unable to set ResultSet: " + resultSetObjs + " object in Class: " + returnType
                                                + ". " + ThreadContext.get(MDC_SQL_QUERY));
                                    }
                                }
                                seqToField.remove(seqNum);
                                seqNum++;

                            }
                            moreResults = stmt.getMoreResults();
                            updateCount = stmt.getUpdateCount();
                        }

                    }

                    if (seqToField.size() != 0) {
                        throw new SPANException("Stored Procedure didn't return enough resultSets. Expected: " + totalResultSets
                                + ". Actual: " + (seqNum - 1) + ". " + ThreadContext.get(MDC_SQL_QUERY));
                    }

                }
            }
        } finally {
            ThreadContext.removeKey(MDC_SQL_QUERY);
            ThreadContext.remove();
        }
        return (O) spOutput;

    }

    /**
     * @param
     * @return
     */
    private Map<Integer, Field> getMappingFromSeqToField(Map<Field, Annotation> fieldToAnnotationMap) {
        Map<Integer, Field> seqToFieldMap = new HashMap<>();
        Set<Field> fieldSet = fieldToAnnotationMap.keySet();
        fieldSet.iterator().forEachRemaining(field -> {
            com.americanexpress.span.annotation.ResultSet annotation = (com.americanexpress.span.annotation.ResultSet) fieldToAnnotationMap.get(field);
            int seqNum = annotation.seqNum();
            Field prevFieldForSqNum = seqToFieldMap.put(seqNum, field);
            if (prevFieldForSqNum != null) {
                throw new SPANException("@ResultSet should have unique continuous Sequence numbers starting at 1. " + seqToFieldMap.keySet() + ". " + ThreadContext.get(MDC_SQL_QUERY));
            }
        });


        if (!seqToFieldMap.keySet().isEmpty()) {
            // All the keys in keyset are unique.
            Integer[] seqNums = new ArrayList<Integer>(seqToFieldMap.keySet()).toArray(new Integer[0]);
            Arrays.sort(seqNums);

            //The first key should be 1
            if (seqNums[0] != 1) {
                throw new SPANException("@ResultSet should have unique Sequence numbers starting at 1. " + seqToFieldMap.keySet() + ". " + ThreadContext.get(MDC_SQL_QUERY));
            }
            //The last key should be equal to the number of keys in the sequence.
            if (seqNums[seqNums.length - 1] != seqToFieldMap.keySet().size()) {
                throw new SPANException("@ResultSet should have unique Sequence numbers starting at 1. " + seqToFieldMap.keySet() + ". " + ThreadContext.get(MDC_SQL_QUERY));
            }
        }

        return seqToFieldMap;
    }

    private <I, O> String getSPQuery(String spUserDefineKey, Class<I> inputClass, Class<O> returnType) {
        int inputParams = SPANUtility.getUniqueAnnotationFieldsFromClass(inputClass, com.americanexpress.span.annotation.Field.class).keySet().size();
        int outParams  = SPANUtility.getUniqueAnnotationFieldsFromClass(returnType, com.americanexpress.span.annotation.Field.class).keySet().size();

        String paramList = String.join(",", Collections.nCopies(inputParams + outParams, "?"));

        SPUserDefineKey spUserDefineObj = SPANConfigHolder.getInstance().getSPUserDefinedKey(spUserDefineKey);

        return CALL_STATEMENT.replace("%NAME%", spUserDefineObj.getSchema() + "."
                + spUserDefineObj.getProcedure()).replace("%PARAM_LIST%", paramList);
    }

    /**
     * Checks whether there is an implementation associated with inputHandler, outputHandler and resultSetHandler.
     * If there is no implementation, then sets default.
     */
    private void checkAndSetDefaultImpls() {
        if (this.inputHandler == null) {
            this.inputHandler = new InputHandlerImpl<>();
        }

        if (this.outputHandler == null) {
            this.outputHandler = new OutputHandlerImpl<>();
        }

        if (this.resultSetHandler == null) {
            this.resultSetHandler = new ResultSetHandlerImpl();
        }

    }


}

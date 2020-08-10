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
package com.americanexpress.span.core.database.handlers.impl;

import com.americanexpress.span.annotation.Field;
import com.americanexpress.span.core.database.SPExecutor;
import com.americanexpress.span.core.database.handlers.FieldTransformation;
import com.americanexpress.span.core.database.handlers.ResultSetHandler;
import com.americanexpress.span.exceptions.SPANException;
import com.americanexpress.span.utility.SPANUtility;
import com.americanexpress.span.utility.ThreadContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * ResultSetHandlerImpl - Implementation for ResultSetHandler. Processes the output of ResultSet and populates object of type T
 */
public class ResultSetHandlerImpl<T> implements ResultSetHandler<T> {

    /**
     * This method processes SP output parameters from ResultSet object and create instance of object T
     *
     * @param resultSet ResultSet after SP execution
     * @param clazzT    Class Object for the type representing OutputParameters
     * @return Object for OutputParameters
     */

    public List<T> processResultSet(ResultSet resultSet, Class<T> clazzT) {
        List<T> resultSetList = new LinkedList<>();

        try {
            while (resultSet.next()) {
                T returnObject;
                try {
                    returnObject = clazzT.newInstance();
                } catch (ReflectiveOperationException e) {
                    throw new SPANException("Exception while creating object of class: " + clazzT
                            + ". Exception Type: " + e.getClass() + ". Exception Message: " + e.getMessage(), e);
                }

                SPANUtility.getAnnotatedDeclaredFields(clazzT).forEach(field -> {
                    setFieldFromResultSet(resultSet, returnObject, field, field.getAnnotation(Field.class).name(),
                            field.getAnnotation(Field.class).default_value(), field.getAnnotation(Field.class).transformationClass(), clazzT);
                });


                SPANUtility.getAnnotatedDeclaredMethods(clazzT).forEach(method -> {
                    java.lang.reflect.Field field = SPANUtility.getFieldFromMethod(method, clazzT);
                    setFieldFromResultSet(resultSet, returnObject, field, method.getAnnotation(Field.class).name(),
                            method.getAnnotation(Field.class).default_value(), method.getAnnotation(Field.class).transformationClass(), clazzT);
                });
                resultSetList.add(returnObject);
            }
        } catch (SQLException e) {
            throw new SPANException("Exception occurred while processing ResultSet. Exception Message: " + e.getMessage(), e);
        }

        return resultSetList;

    }


    /***
     * Populates a single field of object of class Type T. If the field doesn't exist, it uses the default value. The function transforms the default value
     * as String to appropriate type. In case of exception in parsing, the value will be thrown to the calling program.
     *
     * @param resultSet             ResultSet from SP execution
     * @param outputParameters      OutputParameters - this object will be populated with values from ResultSet
     * @param field                 Datamember of class OutputParameters
     * @param annotationName        name parameter of SPAN annotation @Field
     * @param annotationDefaultVal  Value of the annotation
     * @param clazzT                Class object of Type T
     */
    private void setFieldFromResultSet(final ResultSet resultSet, final T outputParameters, final java.lang.reflect.Field field,
                                       final String annotationName, final String annotationDefaultVal,
                                       final Class<? extends FieldTransformation> transformationClass, final Class<T> clazzT) {
        try {
            ThreadContext.set(SPExecutor.MDC_FIELD, field.getType() + " " + field.getName());

            Object fieldValue = resultSet.getObject(annotationName);

            if (fieldValue == null) {
                fieldValue = annotationDefaultVal;
            }

            field.setAccessible(true);
            field.set(outputParameters, transformationClass.newInstance().transform(fieldValue, field.getType()));

        } catch (Exception e) {
            throw new SPANException("Exception while populating ResultSet from class: " + clazzT
                    + ". Exception Type: " + e.getClass() + ". Exception Message: " + e.getMessage() + " Query: " + ThreadContext.get(SPExecutor.MDC_SQL_QUERY) + " Field: "
                    + ThreadContext.get(SPExecutor.MDC_FIELD), e);
        }  finally {
            ThreadContext.removeKey(SPExecutor.MDC_FIELD);
        }
    }


}

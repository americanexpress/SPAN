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
import com.americanexpress.span.core.database.handlers.FieldTransformation;
import com.americanexpress.span.core.database.handlers.OutputHandler;
import com.americanexpress.span.exceptions.SPANException;
import com.americanexpress.span.utility.ThreadContext;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

import static com.americanexpress.span.core.database.SPExecutor.MDC_FIELD;
import static com.americanexpress.span.core.database.SPExecutor.MDC_SQL_QUERY;
import static com.americanexpress.span.utility.SPANUtility.*;

/**
 * OutputHandlerImpl - Implementation for OutputHandler. Processes the output of Callable statement and populates OutputParameter Object
 * @param <T>
 */
public class OutputHandlerImpl<T> implements OutputHandler<T> {


    @Override
    public void registerOutputParameters(CallableStatement callableStatement, Class<T> classT) {
        try {
            for (java.lang.reflect.Field field : getAnnotatedDeclaredFields(classT)) {
                if (field.getType() == Integer.TYPE || field.getType() == Integer.class) {
                    callableStatement.registerOutParameter(field.getAnnotation(Field.class).name(), Types.INTEGER);
                } else if (field.getType() == Float.TYPE || field.getType() == Float.class) {
                    callableStatement.registerOutParameter(field.getAnnotation(Field.class).name(), Types.FLOAT);
                } else if (field.getType() == Double.TYPE || field.getType() == Double.class) {
                    callableStatement.registerOutParameter(field.getAnnotation(Field.class).name(), Types.DOUBLE);
                } else if (field.getType() == String.class) {
                    callableStatement.registerOutParameter(field.getAnnotation(Field.class).name(), Types.VARCHAR);
                } else if (field.getType() == BigDecimal.class || field.getType() == BigInteger.class) {
                    callableStatement.registerOutParameter(field.getAnnotation(Field.class).name(), Types.DOUBLE);
                } else if (field.getType() == Boolean.class || field.getType() == Boolean.TYPE) {
                    callableStatement.registerOutParameter(field.getAnnotation(Field.class).name(), Types.BOOLEAN);
                } else {
                    callableStatement.registerOutParameter(field.getAnnotation(Field.class).name(), Types.VARCHAR);
                }
            }
        } catch (SQLException sqle) {
            throw new SPANException("Exception occurred setting OutputParameters. Exception: " + sqle.getMessage(), sqle);
        }

    }

    /***
     * This method processes SP output parameters from Callable Statement object and create instance of object T (output parameters)
     *
     * @param callableStatement Callable Statement after SP execution
     * @param clazzT            Class Object for the type representing OutputParameters
     * @return                  Object for OutputParameters
     */
    public T processSPOutputParameters(final CallableStatement callableStatement, final Class<T> clazzT)  {

        T returnObject;
        try {
            returnObject = clazzT.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new SPANException("Exception while creating object of class: " + clazzT
                    + ". Exception Type: " + e.getClass() + ". Exception Message: " + e.getMessage(), e);
        }

        getAnnotatedDeclaredFields(clazzT).forEach(field -> {
            setFieldFromCallableStmt(callableStatement, returnObject, field, field.getAnnotation(Field.class).name(),
                    field.getAnnotation(Field.class).default_value(),  field.getAnnotation(Field.class).transformationClass(), clazzT);
        });


        getAnnotatedDeclaredMethods(clazzT).forEach(method -> {
            java.lang.reflect.Field field = getFieldFromMethod(method, clazzT);
            setFieldFromCallableStmt(callableStatement, returnObject, field, method.getAnnotation(Field.class).name(),
                    method.getAnnotation(Field.class).default_value(), method.getAnnotation(Field.class).transformationClass(), clazzT);
        });


        return returnObject;
    }

    /***
     * Populates a single field of object of class Type T
     *
     * @param callableStatement     Callable Statement after SP execution
     * @param outputParameters      OutputParameters - this object will be populated with values from Callable statement
     * @param field                 Datamember of class OutputParameters
     * @param annotationName        name parameter of SPAN annotation @Field
     * @param annotationDefaultVal  Default value of the annotation
     * @param transformationClass   The custom transformation class to transform the value from callable statement to the field.
     * @param clazzT                Class of type T
     *
     */
    private void setFieldFromCallableStmt(final CallableStatement callableStatement, final T outputParameters, final java.lang.reflect.Field field,
                                          final String annotationName, final String annotationDefaultVal,
                                          final Class<? extends FieldTransformation> transformationClass, final Class<T> clazzT) {
        try {
            ThreadContext.set(MDC_FIELD, field.getType() + " " + field.getName());

            Object fieldValue = callableStatement.getObject(annotationName);

            if (fieldValue == null) {
                fieldValue = annotationDefaultVal;
            }

            field.setAccessible(true);
            field.set(outputParameters, transformationClass.newInstance().transform(fieldValue, field.getType()));

        } catch (Exception e) {
            throw new SPANException("Exception while setting object of class: " + clazzT
                    + ". Exception Type: " + e.getClass() + ". Exception Message: " + e.getMessage() + " Query: " + ThreadContext.get(MDC_SQL_QUERY) + " Field: "
                    + ThreadContext.get(MDC_FIELD), e);
        } finally {
            ThreadContext.removeKey(MDC_FIELD);
        }
    }


}

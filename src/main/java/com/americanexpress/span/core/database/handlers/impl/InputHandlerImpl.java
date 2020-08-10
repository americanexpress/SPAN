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
import com.americanexpress.span.core.database.handlers.InputHandler;
import com.americanexpress.span.exceptions.SPANException;
import com.americanexpress.span.utility.SPANUtility;
import com.americanexpress.span.utility.ThreadContext;

import java.sql.CallableStatement;

/**
 * InputHandler - Will do the following
 * 1) Extracts the field and method annotation name and defaultValue from the input Pojo class
 * 2) Processes the input for Callable statement Set the input in the callable statement
 */
public class InputHandlerImpl<T> implements InputHandler<T> {

    /**
     * This method extracts the field name and defaultValue from the Field annotations defined for variables and methods
     *
     * @param callableStatement Callable Statement before SP execution
     * @param inputObject       Input annotation class object
     */
    public void processSPInputParameters(final CallableStatement callableStatement, T inputObject) {


        SPANUtility.getAnnotatedDeclaredMethods(inputObject.getClass()).forEach(method -> {
            java.lang.reflect.Field field = SPANUtility.getFieldFromMethod(method, inputObject.getClass());
            setFieldIntoCallableStmt(callableStatement, field, inputObject, method.getAnnotation(Field.class).default_value(),
                    method.getAnnotation(Field.class).transformationClass(), method.getAnnotation(Field.class).name());

        });

        SPANUtility.getAnnotatedDeclaredFields(inputObject.getClass()).forEach(field -> {
            setFieldIntoCallableStmt(callableStatement, field, inputObject, field.getAnnotation(Field.class).default_value(),
                    field.getAnnotation(Field.class).transformationClass(), field.getDeclaredAnnotation(Field.class).name());
        });


    }


    /**
     * Set the input values in callableStatement
     * If the field value is null/empty then set the default value
     *
     * @param callableStatement   Callable Statement before SP execution
     * @param field               Datamember of class InputParameters
     * @param inputObject         InputObject
     * @param defaultValue        Default value of the field
     * @param transformationClass The custom transformation class to transform the value from callable statement to the field.
     * @param annotationName      Name of the annotation
     */
    private void setFieldIntoCallableStmt(final CallableStatement callableStatement, final java.lang.reflect.Field field, final T inputObject,
                                          final String defaultValue, final Class<? extends FieldTransformation> transformationClass, final String annotationName) {

        try {
            ThreadContext.set(SPExecutor.MDC_FIELD, field.getType() + " " + field.getName());
            field.setAccessible(true);
            Object fieldValue = field.get(inputObject);

            if (fieldValue == null) {
                fieldValue = defaultValue;
            }

            callableStatement.setObject(annotationName, transformationClass.newInstance().transform(fieldValue, Object.class));

        } catch (Exception e) {
            throw new SPANException("Exception while populating CallableStatement from class: " + inputObject.getClass()
                    + ". Exception Type: " + e.getClass() + ". Exception Message: " + e.getMessage() + " Query: " + ThreadContext.get(SPExecutor.MDC_SQL_QUERY) + " Field: "
                    + ThreadContext.get(SPExecutor.MDC_FIELD), e);
        } finally {
            ThreadContext.removeKey(SPExecutor.MDC_FIELD);
        }

    }

}

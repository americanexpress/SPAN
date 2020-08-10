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
package com.americanexpress.span.utility;


import com.americanexpress.span.core.SPANConfigHolder;
import com.americanexpress.span.exceptions.SPANException;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class SPANUtility {

    private SPANUtility () {}

    //make default access
    static final String EXPRESSION_START_STRING = "${";
    static final String EXPRESSION_END_STRING = "}";

    static final String ENCRYPTION_START_STRING = "ENC" + "("; //without expression..
    static final String ENCRYPTION_END_STRING = ")"; //without expression..

    static final String ENCRYPTION_EXPRESSION_START_STRING = ENCRYPTION_START_STRING + EXPRESSION_START_STRING;
    static final String ENCRYPTION_EXPRESSION_END_STRING = EXPRESSION_END_STRING + ENCRYPTION_END_STRING;

    /**
     * Method to lookup environment property
     *
     * @param propertyName
     * @return
     */
    public static Optional<String> getEnvProperty(String propertyName) {

        String envPropertyValue = System.getenv(propertyName);
        if (StringUtils.isEmpty(envPropertyValue)) {
            envPropertyValue = System.getProperty(propertyName);
        }
        if (StringUtils.isEmpty(envPropertyValue)) {
            envPropertyValue = SPANConfigHolder.getInstance().getProperty(propertyName);
        }
        return Optional.ofNullable(envPropertyValue);
    }

    /**
     * Create a method to parse the string, if string start with ${ and end with } .
     *
     * @param expressionString
     * @return
     */
    public static Optional<String> getExpressionValue(String expressionString) {

        String expressionValue = null;

        if (StringUtils.isNotEmpty(expressionString)) {
            expressionValue = StringUtils.substringBetween(expressionString, EXPRESSION_START_STRING, EXPRESSION_END_STRING);
        }

        return Optional.ofNullable(expressionValue);
    }


    /**
     * Create a method to parse the string, if string start with ENC${ and end with } .
     *
     * @param expressionString
     * @return
     */
    public static Optional<String> getEncryptionFromExpression(String expressionString) {

        String expressionValue = null;

        if (StringUtils.isNotEmpty(expressionString)) {
            expressionValue = StringUtils.substringBetween(expressionString, ENCRYPTION_EXPRESSION_START_STRING, EXPRESSION_END_STRING);
        }

        return Optional.ofNullable(expressionValue);
    }


    /**
     * Create a method to parse the string, if string start with ENC( and end with ) .
     *
     * @param expressionString
     * @return
     */
    public static Optional<String> getEncryptionValueFromPlainString(String expressionString) {

        String expressionValue = null;

        if (StringUtils.isNotEmpty(expressionString)) {
            expressionValue = StringUtils.substringBetween(expressionString, ENCRYPTION_START_STRING, ENCRYPTION_END_STRING);
        }

        return Optional.ofNullable(expressionValue);
    }

    /***
     * Utility method to get Field name from Method name
     *
     * @param method       Reflection object Method
     * @param clazz        Class
     * @return Reflection object Field
     */
    public static Field getFieldFromMethod(final Method method, final Class clazz) {
        String name = method.getName();
        String fieldName = "";
        if (name.startsWith("get") || name.startsWith("set")) {
            fieldName = name.substring(3);
        } else if (name.startsWith("is")) {
            fieldName = name.substring(2);
        }
        fieldName = Character.toString(fieldName.charAt(0)).toLowerCase() + fieldName.substring(1);
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new SPANException("Class " + clazz + " doesn't follow Java bean rules for the method: " + method.getName());
        }
    }


    /**
     * Get all Fields that are annotated by SPAN annotation Field.
     *
     * @param clazz Class
     * @return List of fields with annotation Field.class
     */
    public static List<Field> getAnnotatedDeclaredFields(final Class clazz) {
        return getAnnotatedDeclaredFields(clazz, com.americanexpress.span.annotation.Field.class);
    }

    /**
     * Get all Fields that are annotated by SPAN annotation Field.
     *
     * @param baseClass       Class
     * @param annotationClass Annotation Class
     * @return List of fields with annotation annotationClass
     */
    public static List<Field> getAnnotatedDeclaredFields(final Class baseClass, Class annotationClass) {
        Field[] allFields = baseClass.getDeclaredFields();

        List<Field> annotatedFields = new LinkedList<>();

        Arrays.stream(allFields).forEach(field -> {
            if (field.isAnnotationPresent(annotationClass)) {
                annotatedFields.add(field);
            }
        });

        return annotatedFields;
    }

    /**
     * Get all Fields that are annotated by SPAN annotation Field.
     *
     * @param clazz Class
     * @return List of methods with annotation Field.class
     */
    public static List<Method> getAnnotatedDeclaredMethods(final Class clazz) {
        return getAnnotatedDeclaredMethods(clazz, com.americanexpress.span.annotation.Field.class);
    }

    /**
     * Get all Fields that are annotated by SPAN annotation Field.
     *
     * @param baseClass       Base Class
     * @param annotationClass Annotation Class
     * @return List of methods with annotation annotationClass
     */
    public static List<Method> getAnnotatedDeclaredMethods(final Class baseClass, final Class annotationClass) {
        Method[] allMethods = baseClass.getDeclaredMethods();

        List<Method> annotatedMethods = new LinkedList<>();

        Arrays.stream(allMethods).forEach(method -> {
            if (method.isAnnotationPresent(annotationClass)) {
                annotatedMethods.add(method);
            }
        });

        return annotatedMethods;
    }


    /**
     * Get Unique annotation fields (i.e. all fields that have annotation on the main field, or getter or setter method.
     *
     * @param inputClass
     * @param annotationClass
     * @return
     */
    public static Map<Field, Annotation> getUniqueAnnotationFieldsFromClass(final Class inputClass, final Class annotationClass) {
        Map<Field, Annotation> fieldToAnnotation = getAnnotatedDeclaredFields(inputClass, annotationClass).stream().collect(Collectors.toMap(o -> o,
                o -> o.getAnnotation(annotationClass)));

        fieldToAnnotation.putAll(getAnnotatedDeclaredMethods(inputClass, annotationClass).stream()
                .collect(Collectors.toMap(method -> getFieldFromMethod(method, inputClass), method -> method.getAnnotation(annotationClass))));

        return fieldToAnnotation;

    }


}

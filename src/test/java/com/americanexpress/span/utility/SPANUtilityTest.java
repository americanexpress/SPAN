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

import com.americanexpress.span.annotation.Field;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.*;

public class SPANUtilityTest {

    @Test
    public void testGetEnvProperty() {
        Optional<String> expressionValue = SPANUtility.getEnvProperty("${testExpression}");
        assertNotNull(expressionValue);
        assertFalse(expressionValue.isPresent());
    }

    @Test
    public void testGetExpressionValue() {

        Optional<String> expressionValue = SPANUtility.getExpressionValue("${testExpression}");
        assertEquals("testExpression", expressionValue.get());

    }

    @Test
    public void testGetExpressionInvalidValue() {

        Optional<String> expressionValue = SPANUtility.getExpressionValue("testExpression");
        assertNotNull(expressionValue);
        assertFalse(expressionValue.isPresent());

    }

    @Test
    public void testGetEncryptionFromExpression() {

        Optional<String> expressionValue = SPANUtility.getEncryptionFromExpression("ENC(${testExpression})");
        assertEquals("testExpression", expressionValue.get());

    }

    @Test
    public void testGetConfigValueExpressionValueInvalidExpression() {

        Optional<String> expressionValue = SPANUtility.getEncryptionFromExpression("${expressionValue}");
        assertNotNull(expressionValue);
        assertFalse(expressionValue.isPresent());

    }

    @Test
    public void testGetEncryptionValueFromPlainString() {

        Optional<String> expressionValue = SPANUtility.getEncryptionValueFromPlainString("ENC(expressionValue)");
        assertEquals("expressionValue", expressionValue.get());

    }


    @Test
    public void testGetEncryptionValueFromPlainStringInvalidExpression() {

        Optional<String> expressionValue = SPANUtility.getEncryptionValueFromPlainString("ENC11expressionValue)");
        assertNotNull(expressionValue);
        assertFalse(expressionValue.isPresent());


    }


    /**
     * Nested Class for testing AnnotatedDeclaredFields
     */
    public class FieldAnnotationTestClass {

        @Field(name = "fieldInteger")
        private Integer fieldInteger;

        @Field(name = "fieldString")
        private String fieldString;

        @Field(name = "fieldInt")
        private int fieldInt;

    }

    /**
     * Nested Class for testing AnnotatedDeclaredMethods
     */
    public class MethodAnnotationTestClass {

        private String fieldString;
        private int fieldInt;

        @Field(name = "getFieldString")
        public String getFieldString() {
            return fieldString;
        }

        @Field(name = "getFieldInt")
        public int getFieldInt() {
            return fieldInt;
        }

    }

    @Test
    public void testEmptyAnnotatedDeclaredMethods() {

        List<java.lang.reflect.Method> methods = SPANUtility.getAnnotatedDeclaredMethods(FieldAnnotationTestClass.class);
        assertNotNull(methods);
        assertEquals(0, methods.size());
    }

    @Test
    public void testAnnotatedDeclaredMethods() {

        List<java.lang.reflect.Method> methods = SPANUtility.getAnnotatedDeclaredMethods(MethodAnnotationTestClass.class);
        assertEquals(2, methods.size());
    }

    @Test
    public void testGetFieldFromMethod() {

        List<java.lang.reflect.Method> methods = SPANUtility.getAnnotatedDeclaredMethods(MethodAnnotationTestClass.class);
        assertNotNull(methods);
    }

    @Test
    public void testAnnotatedDeclaredFields() {
        List<java.lang.reflect.Field> fields = SPANUtility.getAnnotatedDeclaredFields(FieldAnnotationTestClass.class);
        assertNotNull(fields);
        assertEquals(3, fields.size());
    }

}

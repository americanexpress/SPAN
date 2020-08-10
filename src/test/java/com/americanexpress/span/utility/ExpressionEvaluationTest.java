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

import com.americanexpress.span.core.SPANInitialization;
import com.americanexpress.span.exceptions.SPANException;
import junit.framework.TestCase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.EnvironmentVariables;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.americanexpress.span.core.SPANConfigHolderTest.resetHoldSPANConfigForTesting;

@RunWith(JUnit4.class)
public class ExpressionEvaluationTest extends TestCase {

    private ExpressionEvaluation expressionEvaluation = ExpressionEvaluation.getInstance();

    @Rule
    public final EnvironmentVariables environmentVariables = new EnvironmentVariables();


    @Test
    public void testEvaluateExpressionPlainString() {
        String expression = "test";
        String result = expressionEvaluation.evaluate(expression);
        assertEquals(expression, result);
    }


    @Test(expected = SPANException.class)
    public void testEvaluateExpressionEveluationStringFail() throws SPANException {
        String expression = "${test}";
        String result = expressionEvaluation.evaluate(expression);
    }

    @Test
    public void testEvaluateExpressionEveluationString() throws SPANException {

        environmentVariables.set("testkey", "testvalue");
        String expression = "${testkey}";
        String result = expressionEvaluation.evaluate(expression);
        assertEquals(result, "testvalue");
    }

    @Test(expected = SPANException.class)
    public void testEncryptionExpressionFail() throws SPANException {
        String expression = "ENC(${test})";
        expressionEvaluation.evaluate(expression);
    }


    @Test
    public void testPlainString() throws SPANException {
        String expression = "testing";
        String result = expressionEvaluation.evaluate(expression);
        assertEquals(expression, result);
    }


    @Test
    public void testEvaluateExpressionEveluationStringFromSystemProperties() throws SPANException {

        System.setProperty("testkey", "testvalue");
        String expression = "${testkey}";
        String result = expressionEvaluation.evaluate(expression);
        assertEquals(result, "testvalue");
    }

    @Test
    public void testEvaluateExpressionEveluationStringFromSystemPropertiesFile() throws SPANException, ClassNotFoundException, IllegalAccessException, NoSuchFieldException {
        resetHoldSPANConfigForTesting();
        SPANInitialization.initialize("SampleSPANConfigWithProperties.yaml");

        String expression = "${databasepassword}";
        String result = expressionEvaluation.evaluate(expression);
        assertEquals(result, "test123");
    }


}

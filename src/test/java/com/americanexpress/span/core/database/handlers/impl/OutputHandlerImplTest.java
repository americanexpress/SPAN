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
import com.americanexpress.span.core.SPANInitialization;
import com.americanexpress.span.core.database.handlers.FieldTransformation;
import com.americanexpress.span.exceptions.SPANException;
import com.americanexpress.span.utility.PropertyConfiguration;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.Map;

import static com.americanexpress.span.core.SPANConfigHolderTest.resetHoldSPANConfigForTesting;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

/***
 * Test class for OutputHandler
 */
@RunWith(MockitoJUnitRunner.class)
public class OutputHandlerImplTest {

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();


    /**
     * Class for testing OutputHandlerImpl. OutputHandlerImpl maps output parameters from Stored Procedure Execution to Outputclass.
     * This class will be used to test all datatypes supported by SPAN framework Field annotation.
     * In this case @Field annotation is present on data members of the class.
     */
    public static class SPOutputTestClass {

        @Field(name = "outputParamString")
        private String fieldString;

        @Field(name = "outputParamInt")
        private int fieldInt;

        @Field(name = "outputParamInteger")
        private Integer fieldInteger;

        @Field(name = "outputParamFloat")
        private float fieldFloat;

        @Field(name = "outputParameterFloatObj")
        private Float fieldFloatObj;

        @Field(name = "outputParameterDouble")
        private double fieldDouble;

        @Field(name = "outputParameterDoubleObj")
        private Double fieldDoubleObj;

        @Field(name = "outputParameterBigInteger")
        private BigInteger fieldBigInteger;

        @Field(name = "outputParameterBigDecimal")
        private BigDecimal fieldBigDecimal;

        @Field(name = "outputParameterBoolean")
        private boolean fieldBoolean;

        @Field(name = "outputParameterBooleanObj")
        private Boolean fieldBooleanObj;


        public String getFieldString() {
            return fieldString;
        }

        public void setFieldString(String fieldString) {
            this.fieldString = fieldString;
        }

        public int getFieldInt() {
            return fieldInt;
        }

        public void setFieldInt(int fieldInt) {
            this.fieldInt = fieldInt;
        }

        public Integer getFieldInteger() {
            return fieldInteger;
        }

        public void setFieldInteger(Integer fieldInteger) {
            this.fieldInteger = fieldInteger;
        }

        public float getFieldFloat() {
            return fieldFloat;
        }

        public void setFieldFloat(float fieldFloat) {
            this.fieldFloat = fieldFloat;
        }

        public Float getFieldFloatObj() {
            return fieldFloatObj;
        }

        public void setFieldFloatObj(Float fieldFloatObj) {
            this.fieldFloatObj = fieldFloatObj;
        }

        public double getFieldDouble() {
            return fieldDouble;
        }

        public void setFieldDouble(double fieldDouble) {
            this.fieldDouble = fieldDouble;
        }

        public Double getFieldDoubleObj() {
            return fieldDoubleObj;
        }

        public void setFieldDoubleObj(Double fieldDoubleObj) {
            this.fieldDoubleObj = fieldDoubleObj;
        }

        public BigInteger getFieldBigInteger() {
            return fieldBigInteger;
        }

        public void setFieldBigInteger(BigInteger fieldBigInteger) {
            this.fieldBigInteger = fieldBigInteger;
        }

        public BigDecimal getFieldBigDecimal() {
            return fieldBigDecimal;
        }

        public void setFieldBigDecimal(BigDecimal fieldBigDecimal) {
            this.fieldBigDecimal = fieldBigDecimal;
        }

        public boolean isFieldBoolean() {
            return fieldBoolean;
        }

        public void setFieldBoolean(boolean fieldBoolean) {
            this.fieldBoolean = fieldBoolean;
        }

        public Boolean getFieldBooleanObj() {
            return fieldBooleanObj;
        }

        public void setFieldBooleanObj(Boolean fieldBooleanObj) {
            this.fieldBooleanObj = fieldBooleanObj;
        }

    }

    /**
     * Class for testing OutputHandlerImpl. OutputHandlerImpl maps output parameters from Stored Procedure Execution to Outputclass.
     * This class will be used to test all datatypes supported by SPAN framework Field annotation.
     * In this case @Field annotation is present on getter methods of the class.
     */
    public static class SPOutputTestMethodsClass {

        private String fieldString;
        private int fieldInt;
        private Integer fieldInteger;
        private float fieldFloat;
        private Float fieldFloatObj;
        private double fieldDouble;
        private Double fieldDoubleObj;
        private BigInteger fieldBigInteger;
        private BigDecimal fieldBigDecimal;
        private boolean fieldBoolean;
        private Boolean fieldBooleanObj;

        @Field(name = "outputParamString")
        public String getFieldString() {
            return fieldString;
        }

        @Field(name = "outputParamInt")
        public int getFieldInt() {
            return fieldInt;
        }

        @Field(name = "outputParamInteger")
        public Integer getFieldInteger() {
            return fieldInteger;
        }

        @Field(name = "outputParamFloat")
        public float getFieldFloat() {
            return fieldFloat;
        }

        @Field(name = "outputParameterFloatObj")
        public Float getFieldFloatObj() {
            return fieldFloatObj;
        }

        @Field(name = "outputParameterDouble")
        public double getFieldDouble() {
            return fieldDouble;
        }

        @Field(name = "outputParameterDoubleObj")
        public Double getFieldDoubleObj() {
            return fieldDoubleObj;
        }

        @Field(name = "outputParameterBigInteger")
        public BigInteger getFieldBigInteger() {
            return fieldBigInteger;
        }

        @Field(name = "outputParameterBigDecimal")
        public BigDecimal getFieldBigDecimal() {
            return fieldBigDecimal;
        }

        @Field(name = "outputParameterBoolean")
        public boolean isFieldBoolean() {
            return fieldBoolean;
        }

        @Field(name = "outputParameterBooleanObj")
        public Boolean getFieldBooleanObj() {
            return fieldBooleanObj;
        }
    }

    /**
     * Class for testing OutputHandlerImpl. OutputHandlerImpl maps output parameters from Stored Procedure Execution to Outputclass.
     * This class will be used to test all datatypes supported by SPAN framework Field annotation on data fields.
     * In this case @Field annotation also has default values and default values are used assuming SP didn't return anything.
     */
    public static class SPOutputDefaultTestClass {

        @Field(name = "outputParamString", default_value = "fieldAValueDefault")
        private String fieldString;

        @Field(name = "outputParamInt", default_value = "99")
        private int fieldInt;

        @Field(name = "outputParamInteger", default_value = "999")
        private Integer fieldInteger;

        @Field(name = "outputParamFloat", default_value = "99.99")
        private float fieldFloat;

        @Field(name = "outputParameterFloatObj", default_value = "999.99")
        private Float fieldFloatObj;

        @Field(name = "outputParameterDouble", default_value = "9999.99")
        private double fieldDouble;

        @Field(name = "outputParameterDoubleObj", default_value = "99999.99")
        private Double fieldDoubleObj;

        @Field(name = "outputParameterBigInteger", default_value = "9999")
        private BigInteger fieldBigInteger;

        @Field(name = "outputParameterBigDecimal", default_value = "99999")
        private BigDecimal fieldBigDecimal;

        @Field(name = "outputParameterBoolean", default_value = "false")
        private boolean fieldBoolean;

        @Field(name = "outputParameterBooleanObj", default_value = "false")
        private Boolean fieldBooleanObj;

        public String getFieldString() {
            return fieldString;
        }

        public void setFieldString(String fieldString) {
            this.fieldString = fieldString;
        }

        public int getFieldInt() {
            return fieldInt;
        }

        public void setFieldInt(int fieldInt) {
            this.fieldInt = fieldInt;
        }

        public Integer getFieldInteger() {
            return fieldInteger;
        }

        public void setFieldInteger(Integer fieldInteger) {
            this.fieldInteger = fieldInteger;
        }

        public float getFieldFloat() {
            return fieldFloat;
        }

        public void setFieldFloat(float fieldFloat) {
            this.fieldFloat = fieldFloat;
        }

        public Float getFieldFloatObj() {
            return fieldFloatObj;
        }

        public void setFieldFloatObj(Float fieldFloatObj) {
            this.fieldFloatObj = fieldFloatObj;
        }

        public double getFieldDouble() {
            return fieldDouble;
        }

        public void setFieldDouble(double fieldDouble) {
            this.fieldDouble = fieldDouble;
        }

        public Double getFieldDoubleObj() {
            return fieldDoubleObj;
        }

        public void setFieldDoubleObj(Double fieldDoubleObj) {
            this.fieldDoubleObj = fieldDoubleObj;
        }

        public BigInteger getFieldBigInteger() {
            return fieldBigInteger;
        }

        public void setFieldBigInteger(BigInteger fieldBigInteger) {
            this.fieldBigInteger = fieldBigInteger;
        }

        public BigDecimal getFieldBigDecimal() {
            return fieldBigDecimal;
        }

        public void setFieldBigDecimal(BigDecimal fieldBigDecimal) {
            this.fieldBigDecimal = fieldBigDecimal;
        }

        public boolean isFieldBoolean() {
            return fieldBoolean;
        }

        public void setFieldBoolean(boolean fieldBoolean) {
            this.fieldBoolean = fieldBoolean;
        }

        public Boolean getFieldBooleanObj() {
            return fieldBooleanObj;
        }

        public void setFieldBooleanObj(Boolean fieldBooleanObj) {
            this.fieldBooleanObj = fieldBooleanObj;
        }

    }


    /**
     * Class for testing OutputHandlerImpl. OutputHandlerImpl maps output parameters from Stored Procedure Execution to Outputclass.
     * This class will be used to test all datatypes supported by SPAN framework Field annotation.
     * In this case @Field annotation is present on setter methods of the class.
     */
    public static class SPOutputTestSetterMethodsClass {

        private String fieldString;
        private int fieldInt;
        private Integer fieldInteger;
        private float fieldFloat;
        private Float fieldFloatObj;
        private double fieldDouble;
        private Double fieldDoubleObj;
        private BigInteger fieldBigInteger;
        private BigDecimal fieldBigDecimal;
        private boolean fieldBoolean;
        private Boolean fieldBooleanObj;

        public String getFieldString() {
            return fieldString;
        }

        public int getFieldInt() {
            return fieldInt;
        }

        public Integer getFieldInteger() {
            return fieldInteger;
        }

        public float getFieldFloat() {
            return fieldFloat;
        }

        public Float getFieldFloatObj() {
            return fieldFloatObj;
        }

        public double getFieldDouble() {
            return fieldDouble;
        }

        public Double getFieldDoubleObj() {
            return fieldDoubleObj;
        }

        public BigInteger getFieldBigInteger() {
            return fieldBigInteger;
        }

        public BigDecimal getFieldBigDecimal() {
            return fieldBigDecimal;
        }

        public boolean isFieldBoolean() {
            return fieldBoolean;
        }

        public Boolean getFieldBooleanObj() {
            return fieldBooleanObj;
        }

        @Field(name = "outputParamInt")
        public void setFieldInt(int fieldInt) {
            this.fieldInt = fieldInt;
        }

        @Field(name = "outputParamInteger")
        public void setFieldInteger(Integer fieldInteger) {
            this.fieldInteger = fieldInteger;
        }

        @Field(name = "outputParamFloat")
        public void setFieldFloat(float fieldFloat) {
            this.fieldFloat = fieldFloat;
        }

        @Field(name = "outputParameterFloatObj")
        public void setFieldFloatObj(Float fieldFloatObj) {
            this.fieldFloatObj = fieldFloatObj;
        }

        @Field(name = "outputParameterDouble")
        public void setFieldDouble(double fieldDouble) {
            this.fieldDouble = fieldDouble;
        }

        @Field(name = "outputParameterDoubleObj")
        public void setFieldDoubleObj(Double fieldDoubleObj) {
            this.fieldDoubleObj = fieldDoubleObj;
        }

        @Field(name = "outputParameterBigInteger")
        public void setFieldBigInteger(BigInteger fieldBigInteger) {
            this.fieldBigInteger = fieldBigInteger;
        }

        @Field(name = "outputParameterBigDecimal")
        public void setFieldBigDecimal(BigDecimal fieldBigDecimal) {
            this.fieldBigDecimal = fieldBigDecimal;
        }

        @Field(name = "outputParameterBoolean")
        public void setFieldBoolean(boolean fieldBoolean) {
            this.fieldBoolean = fieldBoolean;
        }

        @Field(name = "outputParameterBooleanObj")
        public void setFieldBooleanObj(Boolean fieldBooleanObj) {
            this.fieldBooleanObj = fieldBooleanObj;
        }

        @Field(name = "outputParamString")
        public void setFieldString(String fieldString) {
            this.fieldString = fieldString;
        }
    }

    /**
     * Class for testing OutputHandlerImpl. OutputHandlerImpl maps output parameters from Stored Procedure Execution to Outputclass.
     * This class will be used to test datatype not supported by SPAN framework Field annotation.
     */
    public static class SPOutputTestBadClass {

        @Field(name = "outputParamMap")
        private Map<String, String> fieldMap;

        public Map<String, String> getFieldMap() {
            return fieldMap;
        }

        public void setFieldMap(Map<String, String> fieldMap) {
            this.fieldMap = fieldMap;
        }


    }

    /**
     * Class for testing OutputHandlerImpl. OutputHandlerImpl maps output parameters from Stored Procedure Execution to Outputclass.
     * This class will test scenario to ensure that SPOutput class is not an abstract class.
     */
    public static abstract class SPOutputTestAClass {

        @Field(name = "outputParamMap")
        private Map<String, String> fieldMap;

        public Map<String, String> getFieldMap() {
            return fieldMap;
        }

        public void setFieldMap(Map<String, String> fieldMap) {
            this.fieldMap = fieldMap;
        }


    }

    /**
     * Class for testing OutputHandlerImpl. OutputHandlerImpl maps output parameters from Stored Procedure Execution to Outputclass.
     * This class will test scenario where in SPOutput doesn't follow bean conventions.
     */
    public static class SPOutputTestNotABeanClass {
        @Field(name = "outputParameterBooleanObj")
        public Boolean getFieldBooleanObj() {
            return null;
        }

    }


    /**
     * Class for testing OutputHandlerImpl. OutputHandlerImpl maps output parameters from Stored Procedure Execution to Outputclass.
     * The class with test scenario where in default value is not valid and there is no transformationClass annotation.
     */
    public static class SPOutputInvalidDefaultTestClass {

        @Field(name = "outputParamInt", default_value = "NotANumber")
        private int fieldInt;

        public int getFieldInt() {
            return fieldInt;
        }

        public void setFieldInt(int fieldInt) {
            this.fieldInt = fieldInt;
        }

    }


    /**
     * Class for testing OutputHandlerImpl. OutputHandlerImpl maps output parameters from Stored Procedure Execution to Outputclass.
     * The class with test scenario wherein simple transformation class is used for transformation.
     */
    public static class SPOutputTransformTestClass {

        @Field(name = "outputParamString", transformationClass = StringTransformationImpl.class)
        private String fieldString;

        @Field(name = "outputParamInt", transformationClass = IntegerTransformationImpl.class)
        private int fieldInt;

        @Field(name = "outputParamInteger", transformationClass = IntegerTransformationImpl.class)
        private Integer fieldInteger;

        @Field(name = "outputParamFloat", transformationClass = FloatTransformationImpl.class)
        private float fieldFloat;

        @Field(name = "outputParameterFloatObj", transformationClass = FloatTransformationImpl.class)
        private Float fieldFloatObj;

        @Field(name = "outputParameterDouble", transformationClass = DoubleTransformationImpl.class)
        private double fieldDouble;

        @Field(name = "outputParameterDoubleObj", transformationClass = DoubleTransformationImpl.class)
        private Double fieldDoubleObj;

        @Field(name = "outputParameterBigInteger", transformationClass = BigIntegerTransformationImpl.class)
        private BigInteger fieldBigInteger;

        @Field(name = "outputParameterBigDecimal", transformationClass = BigDecimalTransformationImpl.class)
        private BigDecimal fieldBigDecimal;

        @Field(name = "outputParameterBoolean", transformationClass = BooleanTransformationImpl.class)
        private boolean fieldBoolean;

        @Field(name = "outputParameterBooleanObj", transformationClass = BooleanTransformationImpl.class)
        private Boolean fieldBooleanObj;

        public String getFieldString() {
            return fieldString;
        }

        public void setFieldString(String fieldString) {
            this.fieldString = fieldString;
        }

        public int getFieldInt() {
            return fieldInt;
        }

        public void setFieldInt(int fieldInt) {
            this.fieldInt = fieldInt;
        }

        public Integer getFieldInteger() {
            return fieldInteger;
        }

        public void setFieldInteger(Integer fieldInteger) {
            this.fieldInteger = fieldInteger;
        }

        public float getFieldFloat() {
            return fieldFloat;
        }

        public void setFieldFloat(float fieldFloat) {
            this.fieldFloat = fieldFloat;
        }

        public Float getFieldFloatObj() {
            return fieldFloatObj;
        }

        public void setFieldFloatObj(Float fieldFloatObj) {
            this.fieldFloatObj = fieldFloatObj;
        }

        public double getFieldDouble() {
            return fieldDouble;
        }

        public void setFieldDouble(double fieldDouble) {
            this.fieldDouble = fieldDouble;
        }

        public Double getFieldDoubleObj() {
            return fieldDoubleObj;
        }

        public void setFieldDoubleObj(Double fieldDoubleObj) {
            this.fieldDoubleObj = fieldDoubleObj;
        }

        public BigInteger getFieldBigInteger() {
            return fieldBigInteger;
        }

        public void setFieldBigInteger(BigInteger fieldBigInteger) {
            this.fieldBigInteger = fieldBigInteger;
        }

        public BigDecimal getFieldBigDecimal() {
            return fieldBigDecimal;
        }

        public void setFieldBigDecimal(BigDecimal fieldBigDecimal) {
            this.fieldBigDecimal = fieldBigDecimal;
        }

        public boolean isFieldBoolean() {
            return fieldBoolean;
        }

        public void setFieldBoolean(boolean fieldBoolean) {
            this.fieldBoolean = fieldBoolean;
        }

        public Boolean getFieldBooleanObj() {
            return fieldBooleanObj;
        }

        public void setFieldBooleanObj(Boolean fieldBooleanObj) {
            this.fieldBooleanObj = fieldBooleanObj;
        }
    }

    /**
     * BEGIN SECTION - Transformation Classes
     */

    public static class StringTransformationImpl implements FieldTransformation<String> {

        @Override
        public String transform(final Object fieldValue, final Class<String> targetClass) {
            String transformedString = String.valueOf(fieldValue);

            return transformedString;
        }


    }

    public static class IntegerTransformationImpl implements FieldTransformation<Integer> {

        @Override
        public Integer transform(final Object fieldValue, final Class<Integer> targetClass) {
            Integer transformedInteger = (Integer)fieldValue + 20;
            return transformedInteger;
        }

    }

    public static class FloatTransformationImpl implements FieldTransformation<Float> {

        @Override
        public Float transform(final Object fieldValue, final Class<Float> targetClass) {

            Float transformedFloat = (Float)fieldValue + 20;
            return transformedFloat;
        }

    }

    public static class DoubleTransformationImpl implements FieldTransformation<Double> {

        @Override
        public Double transform(final Object fieldValue, final Class<Double> targetClass) {
            Double transformedDouble = (Double)fieldValue + 20;
            return transformedDouble;
        }

    }

    public static class BigIntegerTransformationImpl implements FieldTransformation<BigInteger> {

        @Override
        public BigInteger transform(final Object fieldValue, final Class<BigInteger> targetClass) {

            BigInteger transformedBigInteger = (BigInteger) fieldValue;
            return transformedBigInteger.add(new BigInteger("20"));
        }

    }

    public static class BigDecimalTransformationImpl implements FieldTransformation<BigDecimal> {

        @Override
        public BigDecimal transform(final Object fieldValue, final Class<BigDecimal> targetClass) {
            BigDecimal transformedBigDecimal = (BigDecimal)fieldValue;
            return transformedBigDecimal.add(new BigDecimal(20));
        }

    }

    public static class BooleanTransformationImpl implements FieldTransformation<Boolean> {

        @Override
        public Boolean transform(final Object fieldValue, final Class<Boolean> targetClass) {
            Boolean transformedBoolean = !(Boolean)fieldValue;
            return transformedBoolean;
        }

    }


    @Mock
    CallableStatement callableStatement;

    @BeforeClass
    public static void beforeClass() throws IOException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
        resetHoldSPANConfigForTesting();
        SPANInitialization.initialize(new PropertyConfiguration() {
            public String getSPANConfigFileName(){
                return "SPANConfig.yaml";
           }
        });
    }

    /**
     * Sets up mock object for callableStatement
     *
     * @throws SQLException
     */
    @Before
    public void setupMock() throws SQLException {
        reset(callableStatement);
        when(callableStatement.getObject("outputParamString")).thenReturn("fieldAValue");
        when(callableStatement.getObject("outputParamInt")).thenReturn(100);
        when(callableStatement.getObject("outputParamInteger")).thenReturn(1000);
        when(callableStatement.getObject("outputParamFloat")).thenReturn(10_000.0F);
        when(callableStatement.getObject("outputParameterFloatObj")).thenReturn(100_000.0F);
        when(callableStatement.getObject("outputParameterDouble")).thenReturn(1_000_000.0);
        when(callableStatement.getObject("outputParameterDoubleObj")).thenReturn(10_000_000.0);
        when(callableStatement.getObject("outputParameterBigInteger")).thenReturn(new BigInteger("100000000"));
        when(callableStatement.getObject("outputParameterBigDecimal")).thenReturn(new BigDecimal(1_000_000_000D));
        when(callableStatement.getObject("outputParameterBoolean")).thenReturn(true);
        when(callableStatement.getObject("outputParameterBooleanObj")).thenReturn(Boolean.TRUE);
    }

    @Test
    public void testOutputHandlerTransformation() {


        SPOutputTransformTestClass outputParameterTransformTestClassObj = new OutputHandlerImpl<SPOutputTransformTestClass>()
                .processSPOutputParameters(callableStatement, SPOutputTransformTestClass.class);

        assertEquals("fieldAValue", outputParameterTransformTestClassObj.getFieldString());
        assertEquals(120, outputParameterTransformTestClassObj.getFieldInt());
        assertEquals(Integer.valueOf(1020), outputParameterTransformTestClassObj.getFieldInteger());
        assertEquals(10_020.0F, outputParameterTransformTestClassObj.getFieldFloat());
        assertEquals(100_020.0F, outputParameterTransformTestClassObj.getFieldFloatObj());
        assertEquals(1_000_020.0, outputParameterTransformTestClassObj.getFieldDouble());
        assertEquals(10_000_020.0, outputParameterTransformTestClassObj.getFieldDoubleObj());
        assertEquals(BigInteger.valueOf(100_000_020), outputParameterTransformTestClassObj.getFieldBigInteger());
        assertEquals(0, BigDecimal.valueOf(1_000_000_020D).compareTo(outputParameterTransformTestClassObj.getFieldBigDecimal()));
        assertEquals(false, outputParameterTransformTestClassObj.isFieldBoolean());
        assertEquals(Boolean.FALSE, outputParameterTransformTestClassObj.getFieldBooleanObj());

    }


    @Test
    public void testOutputHandler() {


        SPOutputTestClass outputParameterTestObj = new OutputHandlerImpl<SPOutputTestClass>()
                .processSPOutputParameters(callableStatement, SPOutputTestClass.class);

//        assertEquals("fieldAValue", outputParameterTestObj.getFieldString());
        assertEquals(100, outputParameterTestObj.getFieldInt());
        assertEquals(Integer.valueOf(1000), outputParameterTestObj.getFieldInteger());
        assertEquals(10_000.0F, outputParameterTestObj.getFieldFloat());
        assertEquals(100_000.0F, outputParameterTestObj.getFieldFloatObj());
        assertEquals(1_000_000.0, outputParameterTestObj.getFieldDouble());
        assertEquals(10_000_000.0, outputParameterTestObj.getFieldDoubleObj());
        assertEquals(BigInteger.valueOf(100_000_000), outputParameterTestObj.getFieldBigInteger());
        assertEquals(0, BigDecimal.valueOf(1_000_000_000D).compareTo(outputParameterTestObj.getFieldBigDecimal()));
        assertEquals(true, outputParameterTestObj.isFieldBoolean());
        assertEquals(Boolean.TRUE, outputParameterTestObj.getFieldBooleanObj());
    }


    @Test
    public void testOutputHandlerMap() {
        expectedEx.expect(SPANException.class);
        expectedEx.expectMessage("SPAN framework only support following types: Integer, Float, Double, String, BigDecimal, BigInteger, Boolean. " +
                "interface java.util.Map is not supported");
        new OutputHandlerImpl<SPOutputTestBadClass>()
                .processSPOutputParameters(callableStatement, SPOutputTestBadClass.class);


    }

    @Test
    public void testOutputHandlerGetMethods() {
        SPOutputTestMethodsClass outputParameterTestObj = new OutputHandlerImpl<SPOutputTestMethodsClass>()
                .processSPOutputParameters(callableStatement, SPOutputTestMethodsClass.class);

        assertEquals("fieldAValue", outputParameterTestObj.getFieldString());
        assertEquals(100, outputParameterTestObj.getFieldInt());
        assertEquals(Integer.valueOf(1000), outputParameterTestObj.getFieldInteger());
        assertEquals(10_000.0F, outputParameterTestObj.getFieldFloat());
        assertEquals(100_000.0F, outputParameterTestObj.getFieldFloatObj());
        assertEquals(1_000_000.0, outputParameterTestObj.getFieldDouble());
        assertEquals(10_000_000.0, outputParameterTestObj.getFieldDoubleObj());
        assertEquals(BigInteger.valueOf(100_000_000), outputParameterTestObj.getFieldBigInteger());
        assertEquals(0, BigDecimal.valueOf(1_000_000_000D).compareTo(outputParameterTestObj.getFieldBigDecimal()));
        assertEquals(true, outputParameterTestObj.isFieldBoolean());
        assertEquals(Boolean.TRUE, outputParameterTestObj.getFieldBooleanObj());

    }


    @Test
    public void testOutputHandlerSetMethods() {
        SPOutputTestSetterMethodsClass outputParameterTestObj = new OutputHandlerImpl<SPOutputTestSetterMethodsClass>()
                .processSPOutputParameters(callableStatement, SPOutputTestSetterMethodsClass.class);

        assertEquals("fieldAValue", outputParameterTestObj.getFieldString());
        assertEquals(100, outputParameterTestObj.getFieldInt());
        assertEquals(Integer.valueOf(1000), outputParameterTestObj.getFieldInteger());
        assertEquals(10_000.0F, outputParameterTestObj.getFieldFloat());
        assertEquals(100_000.0F, outputParameterTestObj.getFieldFloatObj());
        assertEquals(1_000_000.0, outputParameterTestObj.getFieldDouble());
        assertEquals(10_000_000.0, outputParameterTestObj.getFieldDoubleObj());
        assertEquals(BigInteger.valueOf(100_000_000), outputParameterTestObj.getFieldBigInteger());
        assertEquals(0, BigDecimal.valueOf(1_000_000_000D).compareTo(outputParameterTestObj.getFieldBigDecimal()));
        assertEquals(true, outputParameterTestObj.isFieldBoolean());
        assertEquals(Boolean.TRUE, outputParameterTestObj.getFieldBooleanObj());

    }

    @Test
    public void testOutputHandlerDefaultMethods() throws SQLException {
        reset(callableStatement);
        when(callableStatement.wasNull()).thenReturn(Boolean.TRUE);

        SPOutputDefaultTestClass outputParameterTestObj = new OutputHandlerImpl<SPOutputDefaultTestClass>()
                .processSPOutputParameters(callableStatement, SPOutputDefaultTestClass.class);

        assertEquals("fieldAValueDefault", outputParameterTestObj.getFieldString());
        assertEquals(99, outputParameterTestObj.getFieldInt());
        assertEquals(Integer.valueOf(999), outputParameterTestObj.getFieldInteger());
        assertEquals(99.99F, outputParameterTestObj.getFieldFloat());
        assertEquals(999.99F, outputParameterTestObj.getFieldFloatObj());
        assertEquals(9999.99D, outputParameterTestObj.getFieldDouble());
        assertEquals(99999.99D, outputParameterTestObj.getFieldDoubleObj());
        assertEquals(BigInteger.valueOf(9999), outputParameterTestObj.getFieldBigInteger());
        assertEquals(0, BigDecimal.valueOf(99999).compareTo(outputParameterTestObj.getFieldBigDecimal()));
        assertEquals(false, outputParameterTestObj.isFieldBoolean());
        assertEquals(Boolean.FALSE, outputParameterTestObj.getFieldBooleanObj());

    }

    @Test
    public void testOutputHandlerAClass() throws SQLException {
        expectedEx.expect(SPANException.class);

        expectedEx.expectMessage("Exception while creating object of class: class com.americanexpress.span.core.database.handlers.impl.OutputHandlerImplTest$SPOutputTestAClass. " +
                "Exception Type: class java.lang.InstantiationException. Exception Message: null");

        new OutputHandlerImpl<SPOutputTestAClass>().processSPOutputParameters(callableStatement, SPOutputTestAClass.class);


    }


    @Test
    public void testOutputHandlerNotABeanClass() {
        expectedEx.expect(SPANException.class);
        expectedEx.expectMessage("Class class com.americanexpress.span.core.database.handlers.impl.OutputHandlerImplTest$SPOutputTestNotABeanClass" +
                " doesn't follow Java bean rules for the method: getFieldBooleanObj");

        new OutputHandlerImpl<SPOutputTestNotABeanClass>().processSPOutputParameters(callableStatement, SPOutputTestNotABeanClass.class);
    }

    @Test
    public void testOutputHandlerInvalidDefaultVal() throws SQLException {

        expectedEx.expect(SPANException.class);
        expectedEx.expectMessage("For input string: \"NotANumber\"");

        reset(callableStatement);
        when(callableStatement.wasNull()).thenReturn(Boolean.TRUE);
        new OutputHandlerImpl<SPOutputInvalidDefaultTestClass>().processSPOutputParameters(callableStatement, SPOutputInvalidDefaultTestClass.class);
    }

    @Test
    public void testOutputHandlerSQLException() throws SQLException {
        reset(callableStatement);
        when(callableStatement.getObject(anyString())).thenThrow(new SQLException("Simulating db failure."));
        expectedEx.expect(SPANException.class);
        expectedEx.expectMessage("Exception while setting object of class: class com.americanexpress.span.core.database.handlers.impl.OutputHandlerImplTest$SPOutputTestClass. " +
                "Exception Type: class java.sql.SQLException. Exception Message: Simulating db failure. Query: null Field: class java.lang.String fieldString");


        new OutputHandlerImpl<SPOutputTestClass>()
                .processSPOutputParameters(callableStatement, SPOutputTestClass.class);
    }

    public static class SPOutputTypeTransformTestClass {

        @Field(name = "outputParamInteger", transformationClass = StringToIntegerTransformationImpl.class, default_value="123")
        private Integer fieldInteger;

        public Integer getFieldInteger() {
            return fieldInteger;
        }

        public void setFieldInteger(Integer fieldInteger) {
            this.fieldInteger = fieldInteger;
        }


    }

    public static class StringToIntegerTransformationImpl implements FieldTransformation<Integer> {

        @Override
        public Integer transform(final Object fieldValue, final Class<Integer> targetClass) {

            Integer transformedInteger = Integer.parseInt((String)fieldValue);
            return transformedInteger;
        }

    }

    @Test
    public void testOutputHandlerTypeConversion() throws SQLException {

        reset(callableStatement);
        when(callableStatement.getObject("outputParamInteger")).thenReturn("123");

        SPOutputTypeTransformTestClass outputParameterTestObj = new OutputHandlerImpl<SPOutputTypeTransformTestClass>()
                .processSPOutputParameters(callableStatement, SPOutputTypeTransformTestClass.class);

        assertEquals(new Integer(123), outputParameterTestObj.getFieldInteger());
    }

    @Test
    public void testOutputHandlerInvalidTypeConversion() throws SQLException {

        reset(callableStatement);
        when(callableStatement.getObject("outputParamInteger")).thenReturn("abc");

        expectedEx.expect(SPANException.class);
        expectedEx.expectMessage("For input string: \"abc\"");

        new OutputHandlerImpl<SPOutputTypeTransformTestClass>()
                .processSPOutputParameters(callableStatement, SPOutputTypeTransformTestClass.class);

    }

    /**
     * Transformation class for String to Boolean
     *
     */
    public static class StringToBooleanTransformationImpl implements FieldTransformation<Boolean> {

        @Override
        public Boolean transform(final Object fieldValue, final Class<Boolean> targetClass) {


            Boolean booleanValue = null;

            booleanValue = fieldValue.toString().equalsIgnoreCase("YES");
            return booleanValue;
        }

    }

    public static class SPOutputStringToBooleanTransformTestClass {

        @Field(name = "outputParamBoolean", transformationClass = StringToBooleanTransformationImpl.class)
        private Boolean fieldBoolean;

        public Boolean getFieldBoolean() {
            return fieldBoolean;
        }

        public void setFieldBoolean(Boolean fieldBoolean) {
            this.fieldBoolean = fieldBoolean;
        }


    }


    /**
     * Test case for String to Boolean conversion.
     * @throws SQLException
     */
    @Test
    public void testOutputHandlerStringToBooleanConversion() throws SQLException {

        reset(callableStatement);
        when(callableStatement.getObject("outputParamBoolean")).thenReturn("YES");

        SPOutputStringToBooleanTransformTestClass outputParameterTestObj = new OutputHandlerImpl<SPOutputStringToBooleanTransformTestClass>()
                .processSPOutputParameters(callableStatement, SPOutputStringToBooleanTransformTestClass.class);

        assertEquals(new Boolean(true), outputParameterTestObj.getFieldBoolean());

    }


}
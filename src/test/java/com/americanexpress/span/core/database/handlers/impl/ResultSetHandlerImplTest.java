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
import com.americanexpress.span.core.SPANConfigHolderTest;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

/***
 * Test class for ResultSetHandler
 */
@RunWith(MockitoJUnitRunner.class)
public class ResultSetHandlerImplTest {

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();


    /**
     * Class for testing ResultSetHandler. ResultSetHandlerImpl maps Resultset output from Stored Procedure Execution to ResultSet Class.
     * This class will be used to test all datatypes supported by SPAN framework Field annotation.
     * In this case @Field annotation is present on data members of the class.
     */
    public static class ResultSetTestClass {

        @Field(name = "resultSetString")
        private String fieldString;

        @Field(name = "resultSetInt")
        private int fieldInt;

        @Field(name = "resultSetInteger")
        private Integer fieldInteger;

        @Field(name = "resultSetFloat")
        private float fieldFloat;

        @Field(name = "resultSetFloatObj")
        private Float fieldFloatObj;

        @Field(name = "resultSetDouble")
        private double fieldDouble;

        @Field(name = "resultSetDoubleObj")
        private Double fieldDoubleObj;

        @Field(name = "resultSetBigInteger")
        private BigInteger fieldBigInteger;

        @Field(name = "resultSetBigDecimal")
        private BigDecimal fieldBigDecimal;

        @Field(name = "resultSetBoolean")
        private boolean fieldBoolean;

        @Field(name = "resultSetBooleanObj")
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
    }

    /**
     * Class for testing ResultSetHandler. ResultSetHandlerImpl maps Resultset output from Stored Procedure Execution to ResultSet Class.
     * This class will be used to test all datatypes supported by SPAN framework Field annotation.
     * In this case @Field annotation is present on getter methods of the class.
     */
    public static class ResultSetTestMethodsClass {

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

        @Field(name = "resultSetString")
        public String getFieldString() {
            return fieldString;
        }

        @Field(name = "resultSetInt")
        public int getFieldInt() {
            return fieldInt;
        }

        @Field(name = "resultSetInteger")
        public Integer getFieldInteger() {
            return fieldInteger;
        }

        @Field(name = "resultSetFloat")
        public float getFieldFloat() {
            return fieldFloat;
        }

        @Field(name = "resultSetFloatObj")
        public Float getFieldFloatObj() {
            return fieldFloatObj;
        }

        @Field(name = "resultSetDouble")
        public double getFieldDouble() {
            return fieldDouble;
        }

        @Field(name = "resultSetDoubleObj")
        public Double getFieldDoubleObj() {
            return fieldDoubleObj;
        }

        @Field(name = "resultSetBigInteger")
        public BigInteger getFieldBigInteger() {
            return fieldBigInteger;
        }

        @Field(name = "resultSetBigDecimal")
        public BigDecimal getFieldBigDecimal() {
            return fieldBigDecimal;
        }

        @Field(name = "resultSetBoolean")
        public boolean isFieldBoolean() {
            return fieldBoolean;
        }

        @Field(name = "resultSetBooleanObj")
        public Boolean getFieldBooleanObj() {
            return fieldBooleanObj;
        }
    }

    /**
     * Class for testing ResultSetHandler. ResultSetHandlerImpl maps Resultset output from Stored Procedure Execution to ResultSet Class.
     * This class will be used to test all datatypes supported by SPAN framework Field annotation.
     * In this case @Field annotation also has default values and default values are used assuming SP ResultSet didn't return anything.
     */
    public static class ResultSetDefaultTestClass {

        @Field(name = "resultSetString", default_value = "fieldAValueDefault")
        private String fieldString;

        @Field(name = "resultSetInt", default_value = "99")
        private int fieldInt;

        @Field(name = "resultSetInteger", default_value = "999")
        private Integer fieldInteger;

        @Field(name = "resultSetFloat", default_value = "99.99")
        private float fieldFloat;

        @Field(name = "resultSetFloatObj", default_value = "999.99")
        private Float fieldFloatObj;

        @Field(name = "resultSetDouble", default_value = "9999.99")
        private double fieldDouble;

        @Field(name = "resultSetDoubleObj", default_value = "99999.99")
        private Double fieldDoubleObj;

        @Field(name = "resultSetBigInteger", default_value = "9999")
        private BigInteger fieldBigInteger;

        @Field(name = "resultSetBigDecimal", default_value = "99999")
        private BigDecimal fieldBigDecimal;

        @Field(name = "resultSetBoolean", default_value = "false")
        private boolean fieldBoolean;

        @Field(name = "resultSetBooleanObj", default_value = "false")
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
    }


    /**
     * Class for testing ResultSetHandler. ResultSetHandlerImpl maps Resultset output from Stored Procedure Execution to ResultSet Class.
     * This class will be used to test all datatypes supported by SPAN framework Field annotation.
     * In this case @Field annotation is present on setter methods of the class.
     */
    public static class ResultSetTestSetterMethodsClass {

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

        @Field(name = "resultSetInt")
        public void setFieldInt(int fieldInt) {
            this.fieldInt = fieldInt;
        }

        @Field(name = "resultSetInteger")
        public void setFieldInteger(Integer fieldInteger) {
            this.fieldInteger = fieldInteger;
        }

        @Field(name = "resultSetFloat")
        public void setFieldFloat(float fieldFloat) {
            this.fieldFloat = fieldFloat;
        }

        @Field(name = "resultSetFloatObj")
        public void setFieldFloatObj(Float fieldFloatObj) {
            this.fieldFloatObj = fieldFloatObj;
        }

        @Field(name = "resultSetDouble")
        public void setFieldDouble(double fieldDouble) {
            this.fieldDouble = fieldDouble;
        }

        @Field(name = "resultSetDoubleObj")
        public void setFieldDoubleObj(Double fieldDoubleObj) {
            this.fieldDoubleObj = fieldDoubleObj;
        }

        @Field(name = "resultSetBigInteger")
        public void setFieldBigInteger(BigInteger fieldBigInteger) {
            this.fieldBigInteger = fieldBigInteger;
        }

        @Field(name = "resultSetBigDecimal")
        public void setFieldBigDecimal(BigDecimal fieldBigDecimal) {
            this.fieldBigDecimal = fieldBigDecimal;
        }

        @Field(name = "resultSetBoolean")
        public void setFieldBoolean(boolean fieldBoolean) {
            this.fieldBoolean = fieldBoolean;
        }

        @Field(name = "resultSetBooleanObj")
        public void setFieldBooleanObj(Boolean fieldBooleanObj) {
            this.fieldBooleanObj = fieldBooleanObj;
        }

        @Field(name = "resultSetString")
        public void setFieldString(String fieldString) {
            this.fieldString = fieldString;
        }

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
    }

    /**
     * Class for testing ResultSetHandler. ResultSetHandlerImpl maps Resultset output from Stored Procedure Execution to ResultSet Class.
     * This class will be used to test datatype not supported by SPAN framework Field annotation.
     */
    public static class ResultSetTestBadClass {

        @Field(name = "resultSetMap")
        private Map<String, String> fieldMap;


    }


    /**
     * Class for testing ResultSetHandler. ResultSetHandlerImpl maps Resultset output from Stored Procedure Execution to ResultSet Class.
     * This class will test scenario to ensure that ResultSet Output class is not an abstract class.
     */
    public static abstract class ResultSetTestAClass {

        @Field(name = "resultSetMap")
        private Map<String, String> fieldMap;


    }

    /**
     * Class for testing ResultSetHandler. ResultSetHandlerImpl maps Resultset output from Stored Procedure Execution to ResultSet Class.
     * This class will test scenario where in Result Set doesn't follow bean conventions.
     */
    public static class ResultSetTestNotABeanClass {
        @Field(name = "resultSetBooleanObj")
        public Boolean getFieldBooleanObj() {
            return null;
        }

    }


    /**
     * Class for testing ResultSetHandler. ResultSetHandlerImpl maps Resultset output from Stored Procedure Execution to ResultSet Class.
     * This class will test scenario where in Result Set doesn't follow bean conventions.
     */
    public static class ResultSetInvalidDefaultTestClass {

        @Field(name = "resultSetInt", default_value = "NotANumber")
        private int fieldInt;

    }

    @Mock
    ResultSet resultSet;

    @BeforeClass
    public static void beforeClass() throws IOException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
        SPANConfigHolderTest.resetHoldSPANConfigForTesting();
        SPANInitialization.initialize(new PropertyConfiguration() {
            public String getSPANConfigFileName(){
                return "SPANConfig.yaml";
           }
        });
    }

    /**
     * Sets up mock object for resultSet
     *
     * @throws SQLException
     */
    @Before
    public void setupMock() throws SQLException {
        reset(resultSet);
        when(resultSet.next()).thenReturn(true, false);

        when(resultSet.getObject("resultSetString")).thenReturn("fieldAValue");
        when(resultSet.getObject("resultSetInt")).thenReturn(100);
        when(resultSet.getObject("resultSetInteger")).thenReturn(1000);
        when(resultSet.getObject("resultSetFloat")).thenReturn(10_000.0F);
        when(resultSet.getObject("resultSetFloatObj")).thenReturn(100_000.0F);
        when(resultSet.getObject("resultSetDouble")).thenReturn(1_000_000.0);
        when(resultSet.getObject("resultSetDoubleObj")).thenReturn(10_000_000.0);
        when(resultSet.getObject("resultSetBigInteger")).thenReturn(new BigInteger("100000000"));
        when(resultSet.getObject("resultSetBigDecimal")).thenReturn(new BigDecimal(1_000_000_000D));
        when(resultSet.getObject("resultSetBoolean")).thenReturn(true);
        when(resultSet.getObject("resultSetBooleanObj")).thenReturn(Boolean.TRUE);
    }


    @Test
    public void testResultSetHandler() {

        List<ResultSetTestClass> resultSetList = new ResultSetHandlerImpl<ResultSetTestClass>()
                .processResultSet(resultSet, ResultSetTestClass.class);

        ResultSetTestClass resultSetTestObj = resultSetList.get(0);

        assertEquals("fieldAValue", resultSetTestObj.getFieldString());
        assertEquals(100, resultSetTestObj.getFieldInt());
        assertEquals(Integer.valueOf(1000), resultSetTestObj.getFieldInteger());
        assertEquals(10_000.0F, resultSetTestObj.getFieldFloat());
        assertEquals(100_000.0F, resultSetTestObj.getFieldFloatObj());
        assertEquals(1_000_000.0, resultSetTestObj.getFieldDouble());
        assertEquals(10_000_000.0, resultSetTestObj.getFieldDoubleObj());
        assertEquals(BigInteger.valueOf(100_000_000), resultSetTestObj.getFieldBigInteger());
        assertEquals(0, BigDecimal.valueOf(1_000_000_000D).compareTo(resultSetTestObj.getFieldBigDecimal()));
        assertEquals(true, resultSetTestObj.isFieldBoolean());
        assertEquals(Boolean.TRUE, resultSetTestObj.getFieldBooleanObj());
    }


    @Test
    public void testResultSetHandlerMap() {
        expectedEx.expect(SPANException.class);
        expectedEx.expectMessage("SPAN framework only support following types: Integer, Float, Double, String, BigDecimal, BigInteger, Boolean. " +
                "interface java.util.Map is not supported");
        new ResultSetHandlerImpl<ResultSetTestBadClass>()
                .processResultSet(resultSet, ResultSetTestBadClass.class);


    }

    @Test
    public void testResultSetHandlerGetMethods() {
        List<ResultSetTestMethodsClass> resultSetList = new ResultSetHandlerImpl<ResultSetTestMethodsClass>()
                .processResultSet(resultSet, ResultSetTestMethodsClass.class);

        ResultSetTestMethodsClass resultSetTestObj = resultSetList.get(0);
        assertEquals("fieldAValue", resultSetTestObj.getFieldString());
        assertEquals(100, resultSetTestObj.getFieldInt());
        assertEquals(Integer.valueOf(1000), resultSetTestObj.getFieldInteger());
        assertEquals(10_000.0F, resultSetTestObj.getFieldFloat());
        assertEquals(100_000.0F, resultSetTestObj.getFieldFloatObj());
        assertEquals(1_000_000.0, resultSetTestObj.getFieldDouble());
        assertEquals(10_000_000.0, resultSetTestObj.getFieldDoubleObj());
        assertEquals(BigInteger.valueOf(100_000_000), resultSetTestObj.getFieldBigInteger());
        assertEquals(0, BigDecimal.valueOf(1_000_000_000D).compareTo(resultSetTestObj.getFieldBigDecimal()));
        assertEquals(true, resultSetTestObj.isFieldBoolean());
        assertEquals(Boolean.TRUE, resultSetTestObj.getFieldBooleanObj());

    }


    @Test
    public void testResultSetHandlerSetMethods() {
        List<ResultSetTestSetterMethodsClass> resultSetList = new ResultSetHandlerImpl<ResultSetTestSetterMethodsClass>()
                .processResultSet(resultSet, ResultSetTestSetterMethodsClass.class);

        ResultSetTestSetterMethodsClass resultSetTestObj = resultSetList.get(0);
        assertEquals("fieldAValue", resultSetTestObj.getFieldString());
        assertEquals(100, resultSetTestObj.getFieldInt());
        assertEquals(Integer.valueOf(1000), resultSetTestObj.getFieldInteger());
        assertEquals(10_000.0F, resultSetTestObj.getFieldFloat());
        assertEquals(100_000.0F, resultSetTestObj.getFieldFloatObj());
        assertEquals(1_000_000.0, resultSetTestObj.getFieldDouble());
        assertEquals(10_000_000.0, resultSetTestObj.getFieldDoubleObj());
        assertEquals(BigInteger.valueOf(100_000_000), resultSetTestObj.getFieldBigInteger());
        assertEquals(0, BigDecimal.valueOf(1_000_000_000D).compareTo(resultSetTestObj.getFieldBigDecimal()));
        assertEquals(true, resultSetTestObj.isFieldBoolean());
        assertEquals(Boolean.TRUE, resultSetTestObj.getFieldBooleanObj());

    }

    @Test
    public void testResultSetHandlerDefaultMethods() throws SQLException {
        reset(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.wasNull()).thenReturn(Boolean.TRUE);


        List<ResultSetDefaultTestClass> resultSetList = new ResultSetHandlerImpl<ResultSetDefaultTestClass>()
                .processResultSet(resultSet, ResultSetDefaultTestClass.class);

        ResultSetDefaultTestClass resultSetTestObj = resultSetList.get(0);

        assertEquals("fieldAValueDefault", resultSetTestObj.getFieldString());
        assertEquals(99, resultSetTestObj.getFieldInt());
        assertEquals(Integer.valueOf(999), resultSetTestObj.getFieldInteger());
        assertEquals(99.99F, resultSetTestObj.getFieldFloat());
        assertEquals(999.99F, resultSetTestObj.getFieldFloatObj());
        assertEquals(9999.99D, resultSetTestObj.getFieldDouble());
        assertEquals(99999.99D, resultSetTestObj.getFieldDoubleObj());
        assertEquals(BigInteger.valueOf(9999), resultSetTestObj.getFieldBigInteger());
        assertEquals(0, BigDecimal.valueOf(99999).compareTo(resultSetTestObj.getFieldBigDecimal()));
        assertEquals(false, resultSetTestObj.isFieldBoolean());
        assertEquals(Boolean.FALSE, resultSetTestObj.getFieldBooleanObj());


    }

    @Test
    public void testResultSetHandlerAClass() throws SQLException {
        expectedEx.expect(SPANException.class);
        expectedEx.expectMessage("Exception while creating object of class: class com.americanexpress.span.core.database.handlers.impl.ResultSetHandlerImplTest$ResultSetTestAClass. " +
                "Exception Type: class java.lang.InstantiationException. Exception Message: null");

        new ResultSetHandlerImpl<ResultSetTestAClass>().processResultSet(resultSet, ResultSetTestAClass.class);


    }


    @Test
    public void testResultSetHandlerNotABeanClass() throws SQLException {
        expectedEx.expect(SPANException.class);
        expectedEx.expectMessage("Class class com.americanexpress.span.core.database.handlers.impl.ResultSetHandlerImplTest$ResultSetTestNotABeanClass doesn't follow Java bean rules for the method: getFieldBooleanObj");

        new ResultSetHandlerImpl<ResultSetTestNotABeanClass>().processResultSet(resultSet, ResultSetTestNotABeanClass.class);
    }

    @Test
    public void testResultSetHandlerInvalidDefaultVal() throws SQLException {

        expectedEx.expect(SPANException.class);
        expectedEx.expectMessage("Exception while populating ResultSet from class: class com.americanexpress.span.core.database.handlers.impl.ResultSetHandlerImplTest$ResultSetInvalidDefaultTestClass. " +
                "Exception Type: class java.lang.NumberFormatException. Exception Message: For input string: \"NotANumber\" Query: null Field: int fieldInt");
        reset(resultSet);
        when(resultSet.next()).thenReturn(true, false);

        new ResultSetHandlerImpl<ResultSetInvalidDefaultTestClass>().processResultSet(resultSet, ResultSetInvalidDefaultTestClass.class);
    }

    @Test
    public void testResultSetHandlerSQLException() throws SQLException {
        reset(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getObject(anyString())).thenThrow(new SQLException("Simulating db failure."));
        expectedEx.expect(SPANException.class);
        expectedEx.expectMessage("Exception while populating ResultSet from class: class com.americanexpress.span.core.database.handlers.impl.ResultSetHandlerImplTest$ResultSetTestClass. " +
                "Exception Type: class java.sql.SQLException. Exception Message: Simulating db failure. Query: null Field: class java.lang.String fieldString");


        new ResultSetHandlerImpl<ResultSetTestClass>()
                .processResultSet(resultSet, ResultSetTestClass.class);
    }

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

            Integer transformedInteger = (Integer) fieldValue + 20;
            return transformedInteger;
        }

    }

    public static class FloatTransformationImpl implements FieldTransformation<Float> {

        @Override
        public Float transform(final Object fieldValue, final Class<Float> targetClass) {

            Float transformedFloat = (Float) fieldValue + 20;
            return transformedFloat;
        }

    }

    public static class DoubleTransformationImpl implements FieldTransformation<Double> {

        @Override
        public Double transform(final Object fieldValue, final Class<Double> targetClass) {

            Double transformedDouble = (Double) fieldValue + 20;
            return transformedDouble;
        }

    }

    public static class BigDecimalTransformationImpl implements FieldTransformation<BigDecimal> {

        @Override
        public BigDecimal transform(final Object fieldValue, final Class<BigDecimal> targetClass) {

            BigDecimal transformedBigDecimal = (BigDecimal) fieldValue;
            return transformedBigDecimal.add(new BigDecimal(20));
        }

    }

    public static class BigIntegerTransformationImpl implements FieldTransformation<BigInteger> {

        @Override
        public BigInteger transform(final Object fieldValue, final Class<BigInteger> targetClass) {

            BigInteger transformedBigInteger = (BigInteger) fieldValue;
            return transformedBigInteger.add(new BigInteger("20"));
        }

    }


    public static class BooleanTransformationImpl implements FieldTransformation<Boolean> {

        @Override
        public Boolean transform(final Object fieldValue, final Class<Boolean> targetClass) {

            Boolean transformedBoolean = !(Boolean) fieldValue;
            return transformedBoolean;
        }

    }

    public static class ResultSetTestTransformClass {

        @Field(name = "resultSetString", transformationClass = StringTransformationImpl.class)
        private String fieldString;

        @Field(name = "resultSetInt", transformationClass = IntegerTransformationImpl.class)
        private int fieldInt;

        @Field(name = "resultSetInteger", transformationClass = IntegerTransformationImpl.class)
        private Integer fieldInteger;

        @Field(name = "resultSetFloat", transformationClass = FloatTransformationImpl.class)
        private float fieldFloat;

        @Field(name = "resultSetFloatObj", transformationClass = FloatTransformationImpl.class)
        private Float fieldFloatObj;

        @Field(name = "resultSetDouble", transformationClass = DoubleTransformationImpl.class)
        private double fieldDouble;

        @Field(name = "resultSetDoubleObj", transformationClass = DoubleTransformationImpl.class)
        private Double fieldDoubleObj;

        @Field(name = "resultSetBigInteger", transformationClass = BigIntegerTransformationImpl.class)
        private BigInteger fieldBigInteger;

        @Field(name = "resultSetBigDecimal", transformationClass = BigDecimalTransformationImpl.class)
        private BigDecimal fieldBigDecimal;

        @Field(name = "resultSetBoolean", transformationClass = BooleanTransformationImpl.class)
        private boolean fieldBoolean;

        @Field(name = "resultSetBooleanObj", transformationClass = BooleanTransformationImpl.class)
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
    }

    @Test
    public void
    testResultSetHandlerTransformation() {


        List<ResultSetTestTransformClass> listRS = new ResultSetHandlerImpl<ResultSetTestTransformClass>()
                .processResultSet(resultSet, ResultSetTestTransformClass.class);

        ResultSetTestTransformClass resultSetTestObj = listRS.get(0);

        assertEquals("fieldAValue", resultSetTestObj.getFieldString());
        assertEquals(120, resultSetTestObj.getFieldInt());
        assertEquals(Integer.valueOf(1020), resultSetTestObj.getFieldInteger());
        assertEquals(10_020.0F, resultSetTestObj.getFieldFloat());
        assertEquals(100_020.0F, resultSetTestObj.getFieldFloatObj());
        assertEquals(1_000_020.0, resultSetTestObj.getFieldDouble());
        assertEquals(10_000_020.0, resultSetTestObj.getFieldDoubleObj());
        assertEquals(BigInteger.valueOf(100_000_020), resultSetTestObj.getFieldBigInteger());
        assertEquals(0, BigDecimal.valueOf(1_000_000_020D).compareTo(resultSetTestObj.getFieldBigDecimal()));
        assertEquals(false, resultSetTestObj.isFieldBoolean());
        assertEquals(Boolean.FALSE, resultSetTestObj.getFieldBooleanObj());
    }

    /**
     * Transformation class for String to Boolean
     */
    public static class StringToBooleanTransformationImpl implements FieldTransformation<Boolean> {

        @Override
        public Boolean transform(final Object fieldValue, final Class<Boolean> targetClass) {

            return  "YES".equalsIgnoreCase(fieldValue.toString());
        }

    }

    public static class ResultSetStringToBooleanTransformTestClass {

        @Field(name = "resultSetBoolean", transformationClass = StringToBooleanTransformationImpl.class)
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
     *
     * @throws SQLException
     */
    @Test
    public void testResultSetHandlerStringToBooleanConversion() throws SQLException {

        reset(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getObject("resultSetBoolean")).thenReturn("YES");

        List<ResultSetStringToBooleanTransformTestClass> listRs = new ResultSetHandlerImpl<ResultSetStringToBooleanTransformTestClass>()
                .processResultSet(resultSet, ResultSetStringToBooleanTransformTestClass.class);

        ResultSetStringToBooleanTransformTestClass resultSetTestObj = listRs.get(0);
        assertEquals(new Boolean(true), resultSetTestObj.getFieldBoolean());

    }


}
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
import com.americanexpress.span.core.database.handlers.InputHandler;
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

import static org.mockito.Mockito.*;


/***
 * Test class for InputHandler
 */
@RunWith(MockitoJUnitRunner.class)
public class InputHandlerImplTest {


    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    public static class SPInputTestClass {

        @Field(name = "getFieldString", default_value = "StringDefaultValue")
        public String getFieldString() {
            return fieldString;
        }

        @Field(name = "setFieldString")
        public void setFieldString(String fieldString) {
            this.fieldString = fieldString;
        }

        @Field(name = "getFieldInt")
        public int getFieldInt() {
            return fieldInt;
        }

        @Field(name = "setFieldInt")
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

        public long getFieldLong() {
            return fieldLong;
        }

        public void setFieldLong(long fieldLong) {
            this.fieldLong = fieldLong;
        }

        @Field(name = "inputParamString")
        public String fieldString = "inputParamStringValue";

        @Field(name = "inputParamInt")
        public int fieldInt = 5;

        @Field(name = "inputParamInteger")
        public Integer fieldInteger;

        @Field(name = "inputParamLong")
        public long fieldLong;

        @Field(name = "inputParamFloat")
        public float fieldFloat = 8.555f;

        @Field(name = "inputParamFloatObj")
        public Float fieldFloatObj = 0f;

        @Field(name = "inputParamDouble")
        public double fieldDouble;

        @Field(name = "inputParamDoubleObj")
        public Double fieldDoubleObj = 7.888d;

        @Field(name = "inputParamBigInteger")
        public BigInteger fieldBigInteger;

        @Field(name = "inputParamBigDecimal")
        public BigDecimal fieldBigDecimal = new BigDecimal(9.9999);

        @Field(name = "inputParamBoolean")
        public Boolean fieldBoolean = false;

    }

    InputHandler inputHandlerImpl = new InputHandlerImpl<SPInputTestClass>();

    @Mock
    CallableStatement callableStatement;

    @BeforeClass
    public static void beforeClass() throws IOException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
        SPANConfigHolderTest.resetHoldSPANConfigForTesting();
        SPANInitialization.initialize("SPANConfig.yaml");
    }

    @Test
    public void TestProcessSPInputs() throws SQLException {

        inputHandlerImpl.processSPInputParameters(callableStatement, new SPInputTestClass());

        verify(callableStatement, atLeast(1)).setObject("inputParamString", "inputParamStringValue");
        verify(callableStatement, atLeast(1)).setObject("inputParamInt", 5);
        verify(callableStatement, atLeast(1)).setObject("inputParamDouble", 0.0d);
        verify(callableStatement, atLeast(1)).setObject("inputParamFloat", 8.555f);
        verify(callableStatement, atLeast(1)).setObject("inputParamFloatObj", Float.valueOf(0));
        verify(callableStatement, atLeast(1)).setObject("inputParamBoolean", false);
        verify(callableStatement, atLeast(1)).setObject("inputParamLong", 0L);
        verify(callableStatement, atLeast(1)).setObject("inputParamDoubleObj", new Double(7.888d));
        verify(callableStatement, atLeast(1)).setObject("inputParamBigDecimal", new BigDecimal(9.9999));
    }


    public static class NoAnnotationsTestClass {


        public Integer getFieldInteger() {
            return fieldInteger;
        }

        public void setFieldInteger(Integer fieldInteger) {
            this.fieldInteger = fieldInteger;
        }


        public BigDecimal getFieldBigDecimal() {
            return fieldBigDecimal;
        }

        public void setFieldBigDecimal(BigDecimal fieldBigDecimal) {
            this.fieldBigDecimal = fieldBigDecimal;
        }


        public Integer fieldInteger;


        public BigDecimal fieldBigDecimal = new BigDecimal(9.9999d);

    }


    @Test
    public void TestNoAnnotationsTestClass() throws SQLException {

        inputHandlerImpl.processSPInputParameters(callableStatement, new NoAnnotationsTestClass());

        verify(callableStatement, never()).setBigDecimal("inputParamString", new BigDecimal(9999.9999));
    }


    public static class SPInputTransformTestClass {

        @Field(name = "inputParamString", transformationClass = StringTransformationImpl.class, default_value = "testString")
        public String fieldString;

        @Field(name = "inputParamInt", transformationClass = IntegerTransformationImpl.class)
        public int fieldInt;

        @Field(name = "inputParamInteger", transformationClass = IntegerTransformationImpl.class)
        public Integer fieldInteger;

        @Field(name = "inputParamFloat", transformationClass = FloatTransformationImpl.class)
        public float fieldFloat;

        @Field(name = "inputParamFloatObj", transformationClass = FloatTransformationImpl.class)
        public Float fieldFloatObj;

        @Field(name = "inputParamDouble", transformationClass = DoubleTransformationImpl.class)
        public double fieldDouble;

        @Field(name = "inputParamDoubleObj", transformationClass = DoubleTransformationImpl.class)
        public Double fieldDoubleObj;

        @Field(name = "inputParamBigInteger", transformationClass = BigIntegerTransformationImpl.class)
        public BigInteger fieldBigInteger;

        @Field(name = "inputParamBigDecimal", transformationClass = BigDecimalTransformationImpl.class)
        public BigDecimal fieldBigDecimal;

        @Field(name = "inputParamBoolean", transformationClass = BooleanTransformationImpl.class)
        public boolean fieldBoolean;

        @Field(name = "inputParamBooleanObj", transformationClass = BooleanTransformationImpl.class)
        public Boolean fieldBooleanObj;

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

    public static class StringTransformationImpl implements FieldTransformation<String> {

        @Override
        public String transform(final Object fieldValue, final Class<String> targetClass) {
            String transformedString = String.valueOf(fieldValue) ;
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

            BigDecimal transformedBigDecimal = (BigDecimal) fieldValue;
            return transformedBigDecimal.add(new BigDecimal(20));
        }

    }

    public static class BooleanTransformationImpl implements FieldTransformation<Boolean> {

        @Override
        public Boolean transform(final Object fieldValue, final Class<Boolean> targetClass) {

            Boolean transformedBoolean = !(Boolean) fieldValue;
            return transformedBoolean;
        }

    }

    @Test
    public void TestProcessSPInputsTransform() throws SQLException {

        SPInputTransformTestClass inputParameterTransformTestClass = new SPInputTransformTestClass();
        inputParameterTransformTestClass.setFieldString("inputString");
        inputParameterTransformTestClass.setFieldInt(10);
        inputParameterTransformTestClass.setFieldInteger(new Integer(10));
        inputParameterTransformTestClass.setFieldDouble(10.0d);
        inputParameterTransformTestClass.setFieldDoubleObj(new Double(10.0d));
        inputParameterTransformTestClass.setFieldFloat(10.0f);
        inputParameterTransformTestClass.setFieldFloatObj(new Float(10.0f));
        inputParameterTransformTestClass.setFieldBoolean(true);
        inputParameterTransformTestClass.setFieldBooleanObj(new Boolean(true));
        inputParameterTransformTestClass.setFieldBigDecimal(new BigDecimal(10.0));
        inputParameterTransformTestClass.setFieldBigInteger(new BigInteger("10"));


        inputHandlerImpl.processSPInputParameters(callableStatement, inputParameterTransformTestClass);

        verify(callableStatement, atLeast(1)).setObject("inputParamString", "inputString");
        verify(callableStatement, atLeast(1)).setObject("inputParamInt", 30);
        verify(callableStatement, atLeast(1)).setObject("inputParamDouble", 30.0d);
        verify(callableStatement, atLeast(1)).setObject("inputParamFloat", 30.0f);
        verify(callableStatement, atLeast(1)).setObject("inputParamFloatObj", new Float(30.0));
        verify(callableStatement, atLeast(1)).setObject("inputParamBoolean", false);
        verify(callableStatement, atLeast(1)).setObject("inputParamDoubleObj", new Double(30.0d));
        verify(callableStatement, atLeast(1)).setObject("inputParamBigDecimal", new BigDecimal(30.0));
    }

    public static class SPInputDefaultTestClass {

        @Field(name = "inputParamString", default_value = "fieldAValueDefault")
        private String fieldString;

        @Field(name = "inputParamInt", default_value = "99")
        private int fieldInt;

        @Field(name = "inputParamInteger", default_value = "999")
        private Integer fieldInteger;

        @Field(name = "inputParamFloat", default_value = "99.99")
        private float fieldFloat;

        @Field(name = "inputParameterFloatObj", default_value = "999.99")
        private Float fieldFloatObj;

        @Field(name = "inputParameterDouble", default_value = "9999.99")
        private double fieldDouble;

        @Field(name = "inputParameterDoubleObj", default_value = "99999.99")
        private Double fieldDoubleObj;

        @Field(name = "inputParameterBigInteger", default_value = "9999")
        private BigInteger fieldBigInteger;

        @Field(name = "inputParameterBigDecimal", default_value = "99999")
        private BigDecimal fieldBigDecimal;

        @Field(name = "inputParameterBoolean", default_value = "false")
        private boolean fieldBoolean;

        @Field(name = "inputParameterBooleanObj", default_value = "false")
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


}

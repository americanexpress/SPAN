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
package com.americanexpress.span.core.database.comptest.simple;

import com.americanexpress.span.annotation.Field;
import com.americanexpress.span.annotation.ResultSet;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

public class SPOutput {
    @Field(name = "out_str")
    private String outString;

    @Field(name = "out_int")
    private int outInt;

    @Field(name = "out_integer")
    private Integer outInteger;

    @Field(name = "out_long")
    private long outLong;

    @Field(name = "out_longObj")
    private Long outLongObj;


    @Field(name = "out_float")
    private float outFloat;

    @Field(name = "out_floatObj")
    private Float outFloatObj;

    @Field(name = "out_double")
    private float outDouble;

    @Field(name = "out_doubleObj")
    private Double outDoubleObj;

    @Field(name = "out_bigint")
    private BigInteger outBigInteger;

    @Field(name = "out_decimal")
    private BigDecimal outDecimal;

    @Field(name = "out_boolean")
    private boolean outBoolean;

    @Field(name = "out_booleanObj")
    private Boolean outBooleanObj;

    @Field(name = "out_timestamp")
    private LocalDateTime outTimestamp;

    @ResultSet
    List<SPResultSet> listRSObj;

    public String getOutString() {
        return outString;
    }

    public void setOutString(String outString) {
        this.outString = outString;
    }

    public int getOutInt() {
        return outInt;
    }

    public void setOutInt(int outInt) {
        this.outInt = outInt;
    }

    public Integer getOutInteger() {
        return outInteger;
    }

    public void setOutInteger(Integer outInteger) {
        this.outInteger = outInteger;
    }

    public long getOutLong() {
        return outLong;
    }

    public void setOutLong(long outLong) {
        this.outLong = outLong;
    }

    public Long getOutLongObj() {
        return outLongObj;
    }

    public void setOutLongObj(Long outLongObj) {
        this.outLongObj = outLongObj;
    }

    public float getOutFloat() {
        return outFloat;
    }

    public void setOutFloat(float outFloat) {
        this.outFloat = outFloat;
    }

    public Float getOutFloatObj() {
        return outFloatObj;
    }

    public void setOutFloatObj(Float outFloatObj) {
        this.outFloatObj = outFloatObj;
    }

    public float getOutDouble() {
        return outDouble;
    }

    public void setOutDouble(float outDouble) {
        this.outDouble = outDouble;
    }

    public Double getOutDoubleObj() {
        return outDoubleObj;
    }

    public void setOutDoubleObj(Double outDoubleObj) {
        this.outDoubleObj = outDoubleObj;
    }

    public BigInteger getOutBigInteger() {
        return outBigInteger;
    }

    public void setOutBigInteger(BigInteger outBigInteger) {
        this.outBigInteger = outBigInteger;
    }

    public BigDecimal getOutDecimal() {
        return outDecimal;
    }

    public void setOutDecimal(BigDecimal outDecimal) {
        this.outDecimal = outDecimal;
    }

    public boolean isOutBoolean() {
        return outBoolean;
    }

    public void setOutBoolean(boolean outBoolean) {
        this.outBoolean = outBoolean;
    }

    public Boolean getOutBooleanObj() {
        return outBooleanObj;
    }

    public void setOutBooleanObj(Boolean outBooleanObj) {
        this.outBooleanObj = outBooleanObj;
    }

    public LocalDateTime getOutTimestamp() {
        return outTimestamp;
    }

    public void setOutTimestamp(LocalDateTime outTimestamp) {
        this.outTimestamp = outTimestamp;
    }

    public List<SPResultSet> getListRSObj() {
        return listRSObj;
    }

    public void setListRSObj(List<SPResultSet> listRSObj) {
        this.listRSObj = listRSObj;
    }

    @Override
    public String toString() {
        return "SPOutput{" +
                "outString='" + outString + '\'' +
                ", outInt=" + outInt +
                ", outInteger=" + outInteger +
                ", outFloat=" + outFloat +
                ", outFloatObj=" + outFloatObj +
                ", outDouble=" + outDouble +
                ", outDoubleObj=" + outDoubleObj +
                ", outBigInteger=" + outBigInteger +
                ", outDecimal=" + outDecimal +
                ", outBoolean=" + outBoolean +
                ", outBooleanObj=" + outBooleanObj +
                ", listRSObj=" + listRSObj +
                '}';
    }
}

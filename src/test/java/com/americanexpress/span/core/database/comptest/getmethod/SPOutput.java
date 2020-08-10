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
package com.americanexpress.span.core.database.comptest.getmethod;

import com.americanexpress.span.annotation.Field;
import com.americanexpress.span.annotation.ResultSet;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

public class SPOutput {
    private String outString;

    private int outInt;

    private Integer outInteger;

    private long outLong;

    private Long outLongObj;


    private float outFloat;

    private Float outFloatObj;

    private float outDouble;

    private Double outDoubleObj;

    private BigInteger outBigInteger;

    private BigDecimal outDecimal;

    private boolean outBoolean;

    private Boolean outBooleanObj;

    private LocalDateTime outTimestamp;

    List<SPResultSet> listRSObj;

    @Field(name = "out_str")
    public String getOutString() {
        return outString;
    }

    public void setOutString(String outString) {
        this.outString = outString;
    }

    @Field(name = "out_int")
    public int getOutInt() {
        return outInt;
    }

    public void setOutInt(int outInt) {
        this.outInt = outInt;
    }

    @Field(name = "out_integer")
    public Integer getOutInteger() {
        return outInteger;
    }

    public void setOutInteger(Integer outInteger) {
        this.outInteger = outInteger;
    }

    @Field(name = "out_long")
    public long getOutLong() {
        return outLong;
    }

    public void setOutLong(long outLong) {
        this.outLong = outLong;
    }

    @Field(name = "out_longObj")
    public Long getOutLongObj() {
        return outLongObj;
    }

    public void setOutLongObj(Long outLongObj) {
        this.outLongObj = outLongObj;
    }

    @Field(name = "out_float")
    public float getOutFloat() {
        return outFloat;
    }

    public void setOutFloat(float outFloat) {
        this.outFloat = outFloat;
    }

    @Field(name = "out_floatObj")
    public Float getOutFloatObj() {
        return outFloatObj;
    }

    public void setOutFloatObj(Float outFloatObj) {
        this.outFloatObj = outFloatObj;
    }

    @Field(name = "out_double")
    public float getOutDouble() {
        return outDouble;
    }

    public void setOutDouble(float outDouble) {
        this.outDouble = outDouble;
    }

    @Field(name = "out_doubleObj")
    public Double getOutDoubleObj() {
        return outDoubleObj;
    }

    public void setOutDoubleObj(Double outDoubleObj) {
        this.outDoubleObj = outDoubleObj;
    }

    @Field(name = "out_bigint")
    public BigInteger getOutBigInteger() {
        return outBigInteger;
    }

    public void setOutBigInteger(BigInteger outBigInteger) {
        this.outBigInteger = outBigInteger;
    }

    @Field(name = "out_decimal")
    public BigDecimal getOutDecimal() {
        return outDecimal;
    }

    public void setOutDecimal(BigDecimal outDecimal) {
        this.outDecimal = outDecimal;
    }

    @Field(name = "out_boolean")
    public boolean isOutBoolean() {
        return outBoolean;
    }

    public void setOutBoolean(boolean outBoolean) {
        this.outBoolean = outBoolean;
    }

    @Field(name = "out_booleanObj")
    public Boolean getOutBooleanObj() {
        return outBooleanObj;
    }

    public void setOutBooleanObj(Boolean outBooleanObj) {
        this.outBooleanObj = outBooleanObj;
    }

    @Field(name = "out_timestamp")
    public LocalDateTime getOutTimestamp() {
        return outTimestamp;
    }

    public void setOutTimestamp(LocalDateTime outTimestamp) {
        this.outTimestamp = outTimestamp;
    }

    @ResultSet
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

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

import java.math.BigDecimal;
import java.math.BigInteger;

public class SPResultSet {
    @Field(name = "rs_str")
    private String rsString;

    @Field(name = "rs_int")
    private int rsInt;

    @Field(name = "rs_integer")
    private Integer rsInteger;

    @Field(name = "rs_long")
    private long rsLong;

    @Field(name = "rs_longObj")
    private Long rsLongObj;

    @Field(name = "rs_float")
    private float rsFloat;

    @Field(name = "rs_floatObj")
    private Float rsFloatObj;

    @Field(name = "rs_double")
    private float rsDouble;

    @Field(name = "rs_doubleObj")
    private Double rsDoubleObj;

    @Field(name = "rs_bigint")
    private BigInteger rsBigInteger;

    @Field(name = "rs_decimal")
    private BigDecimal rsDecimal;

    @Field(name = "rs_boolean")
    private boolean rsBoolean;

    @Field(name = "rs_booleanObj")
    private Boolean rsBooleanObj;


    public String getRsString() {
        return rsString;
    }

    public void setRsString(String rsString) {
        this.rsString = rsString;
    }

    public int getRsInt() {
        return rsInt;
    }

    public void setRsInt(int rsInt) {
        this.rsInt = rsInt;
    }

    public Integer getRsInteger() {
        return rsInteger;
    }

    public void setRsInteger(Integer rsInteger) {
        this.rsInteger = rsInteger;
    }

    public long getRsLong() {
        return rsLong;
    }

    public void setRsLong(long rsLong) {
        this.rsLong = rsLong;
    }

    public Long getRsLongObj() {
        return rsLongObj;
    }

    public void setRsLongObj(Long rsLongObj) {
        this.rsLongObj = rsLongObj;
    }

    public float getRsFloat() {
        return rsFloat;
    }

    public void setRsFloat(float rsFloat) {
        this.rsFloat = rsFloat;
    }

    public Float getRsFloatObj() {
        return rsFloatObj;
    }

    public void setRsFloatObj(Float rsFloatObj) {
        this.rsFloatObj = rsFloatObj;
    }

    public float getRsDouble() {
        return rsDouble;
    }

    public void setRsDouble(float rsDouble) {
        this.rsDouble = rsDouble;
    }

    public Double getRsDoubleObj() {
        return rsDoubleObj;
    }

    public void setRsDoubleObj(Double rsDoubleObj) {
        this.rsDoubleObj = rsDoubleObj;
    }

    public BigInteger getRsBigInteger() {
        return rsBigInteger;
    }

    public void setRsBigInteger(BigInteger rsBigInteger) {
        this.rsBigInteger = rsBigInteger;
    }

    public BigDecimal getRsDecimal() {
        return rsDecimal;
    }

    public void setRsDecimal(BigDecimal rsDecimal) {
        this.rsDecimal = rsDecimal;
    }

    public boolean isRsBoolean() {
        return rsBoolean;
    }

    public void setRsBoolean(boolean rsBoolean) {
        this.rsBoolean = rsBoolean;
    }

    public Boolean getRsBooleanObj() {
        return rsBooleanObj;
    }

    public void setRsBooleanObj(Boolean rsBooleanObj) {
        this.rsBooleanObj = rsBooleanObj;
    }

    @Override
    public String toString() {
        return "SPResultSet{" +
                "rsString='" + rsString + '\'' +
                ", rsInt=" + rsInt +
                ", rsInteger=" + rsInteger +
                ", rsFloat=" + rsFloat +
                ", rsFloatObj=" + rsFloatObj +
                ", rsDouble=" + rsDouble +
                ", rsDoubleObj=" + rsDoubleObj +
                ", rsBigInteger=" + rsBigInteger +
                ", rsDecimal=" + rsDecimal +
                ", rsBoolean=" + rsBoolean +
                ", rsBooleanObj=" + rsBooleanObj +
                '}';
    }
}

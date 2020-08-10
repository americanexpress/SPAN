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

import java.math.BigDecimal;
import java.math.BigInteger;

public class SPResultSet {
    private String rsString;

    private int rsInt;

    private Integer rsInteger;

    private long rsLong;

    private Long rsLongObj;

    private float rsFloat;

    private Float rsFloatObj;

    private float rsDouble;

    private Double rsDoubleObj;

    private BigInteger rsBigInteger;

    private BigDecimal rsDecimal;

    private boolean rsBoolean;

    private Boolean rsBooleanObj;


    @Field(name = "rs_str")
    public String getRsString() {
        return rsString;
    }

    public void setRsString(String rsString) {
        this.rsString = rsString;
    }

    @Field(name = "rs_int")
    public int getRsInt() {
        return rsInt;
    }

    public void setRsInt(int rsInt) {
        this.rsInt = rsInt;
    }

    @Field(name = "rs_integer")
    public Integer getRsInteger() {
        return rsInteger;
    }

    public void setRsInteger(Integer rsInteger) {
        this.rsInteger = rsInteger;
    }

    @Field(name = "rs_long")
    public long getRsLong() {
        return rsLong;
    }

    public void setRsLong(long rsLong) {
        this.rsLong = rsLong;
    }

    @Field(name = "rs_longObj")
    public Long getRsLongObj() {
        return rsLongObj;
    }

    public void setRsLongObj(Long rsLongObj) {
        this.rsLongObj = rsLongObj;
    }

    @Field(name = "rs_float")
    public float getRsFloat() {
        return rsFloat;
    }

    public void setRsFloat(float rsFloat) {
        this.rsFloat = rsFloat;
    }

    @Field(name = "rs_floatObj")
    public Float getRsFloatObj() {
        return rsFloatObj;
    }

    public void setRsFloatObj(Float rsFloatObj) {
        this.rsFloatObj = rsFloatObj;
    }

    @Field(name = "rs_double")
    public float getRsDouble() {
        return rsDouble;
    }

    public void setRsDouble(float rsDouble) {
        this.rsDouble = rsDouble;
    }

    @Field(name = "rs_doubleObj")
    public Double getRsDoubleObj() {
        return rsDoubleObj;
    }

    public void setRsDoubleObj(Double rsDoubleObj) {
        this.rsDoubleObj = rsDoubleObj;
    }

    @Field(name = "rs_bigint")
    public BigInteger getRsBigInteger() {
        return rsBigInteger;
    }

    public void setRsBigInteger(BigInteger rsBigInteger) {
        this.rsBigInteger = rsBigInteger;
    }

    @Field(name = "rs_decimal")
    public BigDecimal getRsDecimal() {
        return rsDecimal;
    }

    public void setRsDecimal(BigDecimal rsDecimal) {
        this.rsDecimal = rsDecimal;
    }

    @Field(name = "rs_boolean")
    public boolean isRsBoolean() {
        return rsBoolean;
    }

    public void setRsBoolean(boolean rsBoolean) {
        this.rsBoolean = rsBoolean;
    }

    @Field(name = "rs_booleanObj")
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

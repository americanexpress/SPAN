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
package com.americanexpress.span.core.database.comptest.setmethod;

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


    public String getRsString() {
        return rsString;
    }

    @Field(name = "rs_str")
    public void setRsString(String rsString) {
        this.rsString = rsString;
    }

    public int getRsInt() {
        return rsInt;
    }

    @Field(name = "rs_int")
    public void setRsInt(int rsInt) {
        this.rsInt = rsInt;
    }

    public Integer getRsInteger() {
        return rsInteger;
    }

    @Field(name = "rs_integer")
    public void setRsInteger(Integer rsInteger) {
        this.rsInteger = rsInteger;
    }

    public long getRsLong() {
        return rsLong;
    }

    @Field(name = "rs_long")
    public void setRsLong(long rsLong) {
        this.rsLong = rsLong;
    }

    public Long getRsLongObj() {
        return rsLongObj;
    }

    @Field(name = "rs_longObj")
    public void setRsLongObj(Long rsLongObj) {
        this.rsLongObj = rsLongObj;
    }

    public float getRsFloat() {
        return rsFloat;
    }

    @Field(name = "rs_float")
    public void setRsFloat(float rsFloat) {
        this.rsFloat = rsFloat;
    }

    public Float getRsFloatObj() {
        return rsFloatObj;
    }

    @Field(name = "rs_floatObj")
    public void setRsFloatObj(Float rsFloatObj) {
        this.rsFloatObj = rsFloatObj;
    }

    public float getRsDouble() {
        return rsDouble;
    }

    @Field(name = "rs_double")
    public void setRsDouble(float rsDouble) {
        this.rsDouble = rsDouble;
    }

    public Double getRsDoubleObj() {
        return rsDoubleObj;
    }

    @Field(name = "rs_doubleObj")
    public void setRsDoubleObj(Double rsDoubleObj) {
        this.rsDoubleObj = rsDoubleObj;
    }

    public BigInteger getRsBigInteger() {
        return rsBigInteger;
    }

    @Field(name = "rs_bigint")
    public void setRsBigInteger(BigInteger rsBigInteger) {
        this.rsBigInteger = rsBigInteger;
    }

    public BigDecimal getRsDecimal() {
        return rsDecimal;
    }

    @Field(name = "rs_decimal")
    public void setRsDecimal(BigDecimal rsDecimal) {
        this.rsDecimal = rsDecimal;
    }

    public boolean isRsBoolean() {
        return rsBoolean;
    }

    @Field(name = "rs_boolean")
    public void setRsBoolean(boolean rsBoolean) {
        this.rsBoolean = rsBoolean;
    }

    public Boolean getRsBooleanObj() {
        return rsBooleanObj;
    }

    @Field(name = "rs_booleanObj")
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

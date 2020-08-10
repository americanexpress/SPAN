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

import com.americanexpress.span.core.database.SPExecutor;
import com.americanexpress.span.exceptions.SPANException;
import com.americanexpress.span.utility.ThreadContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;

import static junit.framework.TestCase.*;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertThat;

public class FieldTransformationImplTest {

    private final FieldTransformationImpl fieldTransformation = new FieldTransformationImpl();
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @BeforeClass
    public static void beforeClass() {
        ThreadContext.set(SPExecutor.MDC_SQL_QUERY, "test Query{}");
        ThreadContext.set(SPExecutor.MDC_FIELD, "Test Field");
    }

    @AfterClass
    public static void afterClass() {
        ThreadContext.removeKey(SPExecutor.MDC_SQL_QUERY);
        ThreadContext.removeKey(SPExecutor.MDC_FIELD);
    }

    @Test
    public void testIntToBoolean() {
        expectedException.expect(SPANException.class);
        expectedException.expectMessage("Cannot transform type class java.lang.Integer to Boolean. test Query{}. Field: Test Field");
        fieldTransformation.transform(123, Boolean.class);
    }


    @Test
    public void testBooleanToBoolean() {
        assertTrue((Boolean) fieldTransformation.transform(true, Boolean.class));
        assertTrue((Boolean) fieldTransformation.transform(new Boolean(true), Boolean.class));
    }

    @Test
    public void testNull() {
        assertNull(fieldTransformation.transform(null, Boolean.class));
    }

    @Test
    public void testBigInteger() {
        assertEquals(BigInteger.valueOf(10), fieldTransformation.transform(Integer.valueOf(10), BigInteger.class));
        assertEquals(BigInteger.valueOf(10), fieldTransformation.transform(Long.valueOf(10L), BigInteger.class));
        assertEquals(BigInteger.valueOf(10), fieldTransformation.transform(Float.valueOf(10.1F), BigInteger.class));
        assertEquals(BigInteger.valueOf(10), fieldTransformation.transform(Double.valueOf(10.1D), BigInteger.class));
        assertEquals(BigInteger.valueOf(10), fieldTransformation.transform(BigInteger.valueOf(10), BigInteger.class));
        assertEquals(BigInteger.valueOf(10), fieldTransformation.transform(BigDecimal.valueOf(10.1D), BigInteger.class));
        assertEquals(BigInteger.valueOf(10), fieldTransformation.transform("10", BigInteger.class));
    }

    @Test
    public void testBigIntegerExcep() {
        expectedException.expect(SPANException.class);
        expectedException.expectMessage("Conversion from class java.util.HashMap to BigInteger is not supported. test Query{}. Field: Test Field");
        assertEquals(BigInteger.valueOf(10), fieldTransformation.transform(new HashMap<>(), BigInteger.class));
    }

    @Test
    public void testBigDecimal() {
        assertEquals(BigDecimal.valueOf(10.0), fieldTransformation.transform(Integer.valueOf(10), BigDecimal.class));
        assertEquals(BigDecimal.valueOf(10.0), fieldTransformation.transform(Long.valueOf(10L), BigDecimal.class));
        assertThat("Float to BigDecimal Value",
                Math.abs(BigDecimal.valueOf(10.1).subtract((BigDecimal) fieldTransformation.transform(Float.valueOf(10.1F), BigDecimal.class)).doubleValue()),
                lessThanOrEqualTo(0.00001D));
        assertEquals(BigDecimal.valueOf(10.1), fieldTransformation.transform(Double.valueOf(10.1D), BigDecimal.class));
        assertThat("BigInteger Integer to BigDecimal Value",
                Math.abs(BigDecimal.valueOf(10).subtract((BigDecimal) fieldTransformation.transform(BigInteger.valueOf(10), BigDecimal.class)).doubleValue()),
                lessThanOrEqualTo(0.00001D));

        assertThat("Integer to BigDecimal Value",
                Math.abs(BigDecimal.valueOf(10).subtract((BigDecimal) fieldTransformation.transform(Integer.valueOf(10), BigDecimal.class)).doubleValue()),
                lessThanOrEqualTo(0.00001D));

        assertEquals(BigDecimal.valueOf(10.1), fieldTransformation.transform(BigDecimal.valueOf(10.1D), BigDecimal.class));
        assertEquals(BigDecimal.valueOf(10.1), fieldTransformation.transform("10.1", BigDecimal.class));
    }

    @Test
    public void testBigDecimalExcep() {
        expectedException.expect(SPANException.class);
        expectedException.expectMessage("Conversion from class java.util.HashMap to BigDecimal is not supported. test Query{}. Field: Test Field");
        assertEquals(BigDecimal.valueOf(10), fieldTransformation.transform(new HashMap<>(), BigDecimal.class));
    }

    @Test
    public void testFloat() {
        assertEquals(Float.valueOf(10.0F), fieldTransformation.transform(Integer.valueOf(10), Float.class));
        assertEquals(Float.valueOf(10.0F), fieldTransformation.transform(Long.valueOf(10L), Float.class));
        assertEquals(Float.valueOf(10.1F), fieldTransformation.transform(Float.valueOf(10.1F), Float.class));
        assertEquals(Float.valueOf(10.1F), fieldTransformation.transform(Double.valueOf(10.1D), Float.class));
        assertEquals(Float.valueOf(10F), fieldTransformation.transform(BigInteger.valueOf(10), Float.class));
        assertEquals(Float.valueOf(10.1F), fieldTransformation.transform(BigDecimal.valueOf(10.1D), Float.class));
        assertEquals(Float.valueOf(10.1F), fieldTransformation.transform("10.1", Float.class));  }

    @Test
    public void testFloatExcep() {
        expectedException.expect(SPANException.class);
        expectedException.expectMessage("Conversion from class java.util.HashMap to Float is not supported. test Query{}. Field: Test Field");
        assertEquals(Float.valueOf(10), fieldTransformation.transform(new HashMap<>(), Float.class));
    }

    @Test
    public void testInteger() {
        assertEquals(Integer.valueOf(10), fieldTransformation.transform(Integer.valueOf(10), Integer.class));
        assertEquals(Integer.valueOf(10), fieldTransformation.transform(Long.valueOf(10L), Integer.class));
        assertEquals(Integer.valueOf(10), fieldTransformation.transform(Float.valueOf(10.1F), Integer.class));
        assertEquals(Integer.valueOf(10), fieldTransformation.transform(Double.valueOf(10.1D), Integer.class));
        assertEquals(Integer.valueOf(10), fieldTransformation.transform(BigInteger.valueOf(10), Integer.class));
        assertEquals(Integer.valueOf(10), fieldTransformation.transform(BigDecimal.valueOf(10.1D), Integer.class));
        assertEquals(Integer.valueOf(10), fieldTransformation.transform("10", Integer.class));
    }

    @Test
    public void testIntegerExcep() {
        expectedException.expect(SPANException.class);
        expectedException.expectMessage("Conversion from class java.util.HashMap to Integer is not supported. test Query{}. Field: Test Field");
        assertEquals(Integer.valueOf(10), fieldTransformation.transform(new HashMap<>(), Integer.class));
    }

    @Test
    public void testLong() {
        assertEquals(Long.valueOf(10), fieldTransformation.transform(Integer.valueOf(10), Long.class));
        assertEquals(Long.valueOf(10), fieldTransformation.transform(Long.valueOf(10L), Long.class));
        assertEquals(Long.valueOf(10), fieldTransformation.transform(Float.valueOf(10.1F), Long.class));
        assertEquals(Long.valueOf(10), fieldTransformation.transform(Double.valueOf(10.1D), Long.class));
        assertEquals(Long.valueOf(10), fieldTransformation.transform(BigInteger.valueOf(10), Long.class));
        assertEquals(Long.valueOf(10), fieldTransformation.transform(BigDecimal.valueOf(10.1D), Long.class));
        assertEquals(Long.valueOf(10), fieldTransformation.transform("10", Long.class));
    }

    @Test
    public void testLongExcep() {
        expectedException.expect(SPANException.class);
        expectedException.expectMessage("Conversion from class java.util.HashMap to Long is not supported. test Query{}. Field: Test Field");
        assertEquals(Long.valueOf(10), fieldTransformation.transform(new HashMap<>(), Long.class));
    }

    @Test
    public void testDouble() {
        assertEquals(Double.valueOf(10), fieldTransformation.transform(Integer.valueOf(10), Double.class));
        assertEquals(Double.valueOf(10), fieldTransformation.transform(Long.valueOf(10L), Double.class));
        assertThat( "Double Value",Math.abs(Double.valueOf(10.1) - (Double)fieldTransformation.transform(Float.valueOf(10.1F), Double.class)),
                lessThanOrEqualTo(0.0001));
        assertEquals(Double.valueOf(10.1), fieldTransformation.transform(Double.valueOf(10.1D), Double.class));
        assertEquals(Double.valueOf(10), fieldTransformation.transform(BigInteger.valueOf(10), Double.class));
        assertEquals(Double.valueOf(10.1), fieldTransformation.transform(BigDecimal.valueOf(10.1D), Double.class));
        assertEquals(Double.valueOf(10), fieldTransformation.transform("10", Double.class));
    }

    @Test
    public void testDoubleExcep() {
        expectedException.expect(SPANException.class);
        expectedException.expectMessage("Conversion from class java.util.HashMap to Double is not supported. test Query{}. Field: Test Field");
        assertEquals(Double.valueOf(10), fieldTransformation.transform(new HashMap<>(), Double.class));
    }

    @Test
    public void testLocalDate() {
        LocalDate now = LocalDate.now();
        assertEquals(now, fieldTransformation.transform(now, LocalDate.class));
        assertEquals(now, fieldTransformation.transform(LocalDateTime.now(), LocalDate.class));
        assertEquals(now, fieldTransformation.transform(new Timestamp(System.currentTimeMillis()), LocalDate.class));
        assertEquals(now, fieldTransformation.transform(new Date(System.currentTimeMillis()), LocalDate.class));

    }

    @Test
    public void testLocalDateExcep() {
        expectedException.expect(SPANException.class);
        expectedException.expectMessage("Cannot transform type class java.lang.String to LocalDate. test Query{}. Field: Test Field");
        fieldTransformation.transform("", LocalDate.class);
    }

    @Test
    public void testLocalDateTime() {
        LocalDateTime now = LocalDateTime.now();
        assertEquals(now, fieldTransformation.transform(now, LocalDateTime.class));
        assertEquals(now.toLocalDate().atStartOfDay(), fieldTransformation.transform(LocalDate.now(), LocalDateTime.class));
        assertEquals(now, fieldTransformation.transform(Timestamp.valueOf(now), LocalDateTime.class));
        assertEquals(now.toLocalDate().atStartOfDay(), fieldTransformation.transform(new Date(System.currentTimeMillis()), LocalDateTime.class));
    }


    @Test
    public void testLocalDateTimeExcep() {
        expectedException.expect(SPANException.class);
        expectedException.expectMessage("Cannot transform type class java.lang.String to LocalDateTime. test Query{}. Field: Test Field");
        fieldTransformation.transform("", LocalDateTime.class);
    }


}

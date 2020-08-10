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
import com.americanexpress.span.core.database.handlers.FieldTransformation;
import com.americanexpress.span.exceptions.SPANException;
import com.americanexpress.span.utility.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Default Implementation for FieldTransformation. Returns the field Value as is without any transformation
 *
 *
 */
public class FieldTransformationImpl<K> implements FieldTransformation<K> {

    private static final Logger logger = LoggerFactory.getLogger(FieldTransformationImpl.class);


    /**
     * Returns the field Value as is without any transformation
     */
    @Override
    public K transform(final Object fieldValue, final Class<K> targetClass) {

        if (fieldValue == null) {
            return null;
        }

        if (targetClass == Object.class) {
            return (K) fieldValue;
        }

        if (targetClass == Integer.TYPE || targetClass == Integer.class) {
            return (K)getIntValue(fieldValue);
        } else if (targetClass == Float.TYPE || targetClass == Float.class) {
            return (K)getFloatVal(fieldValue);
        } else if (targetClass == Long.TYPE || targetClass == Long.class) {
            return (K)getLongVal(fieldValue);
        } else if (targetClass == Double.TYPE || targetClass == Double.class) {
            return (K)getDoubleVal(fieldValue);
        } else if (targetClass == String.class) {
            return (K)String.valueOf(fieldValue);
        } else if (targetClass == BigDecimal.class) {
            return (K)getBigDecimalVal(fieldValue);
        } else if (targetClass == BigInteger.class) {
            return (K)getBigIntegerVal(fieldValue);
        } else if (targetClass == Boolean.TYPE || targetClass == Boolean.class) {
            return (K) getBooleanVal(fieldValue);
        } else if (targetClass == LocalDate.class) {
            return (K) getLocalDateVal(fieldValue);
        } else if (targetClass == LocalDateTime.class) {
            return (K) getLocalDateTimeVal(fieldValue);
        }

        throw new SPANException("SPAN framework only support following types: Integer, Float, Double, String, BigDecimal, BigInteger, Boolean. " +
                targetClass + " is not supported" + ThreadContext.get(SPExecutor.MDC_SQL_QUERY) + ". Field: " + ThreadContext.get(SPExecutor.MDC_FIELD));

    }

    /**
     * Transforms input object value to LocalDateTime.
     *
     * @param fieldValue    Input Value
     * @return              Transformed Value
     */
    private LocalDateTime getLocalDateTimeVal(Object fieldValue) {
        Class sourceClass = fieldValue.getClass();

        if (sourceClass == LocalDateTime.class) {
            return (LocalDateTime) fieldValue;
        }
        if (sourceClass == LocalDate.class) {
            LocalDate val = (LocalDate) fieldValue;
            return val.atStartOfDay();
        }

        if (sourceClass == Timestamp.class) {
            Timestamp val = (Timestamp) fieldValue;
            return val.toLocalDateTime();
        }

        if (sourceClass == Date.class) {
            Date val = (Date) fieldValue;
            return val.toLocalDate().atStartOfDay();
        }

        throw new SPANException("Cannot transform type " + fieldValue.getClass() + " to LocalDateTime. " + ThreadContext.get(SPExecutor.MDC_SQL_QUERY) + ". Field: " + ThreadContext.get(SPExecutor.MDC_FIELD));
    }


    /**
     * Transforms input object value to LocalDate.
     *
     * @param fieldValue    Input Value
     * @return              Transformed Value
     */
    private LocalDate getLocalDateVal(Object fieldValue) {
        Class sourceClass = fieldValue.getClass();

        if (sourceClass == LocalDate.class) {
            return (LocalDate) fieldValue;
        }
        if (sourceClass == LocalDateTime.class) {
            LocalDateTime val = (LocalDateTime) fieldValue;
            return val.toLocalDate();
        }

        if (sourceClass == Timestamp.class) {
            Timestamp val = (Timestamp) fieldValue;
            return val.toLocalDateTime().toLocalDate();
        }

        if (sourceClass == Date.class) {
            Date val = (Date) fieldValue;
            return val.toLocalDate();
        }

        throw new SPANException("Cannot transform type " + fieldValue.getClass() + " to LocalDate. " + ThreadContext.get(SPExecutor.MDC_SQL_QUERY) + ". Field: " + ThreadContext.get(SPExecutor.MDC_FIELD));
    }


    /**
     * Transforms input object value to Boolean.
     *
     * @param fieldValue    Input Value
     * @return              Transformed Value
     */
    private Boolean getBooleanVal(Object fieldValue) {
        Class sourceClass = fieldValue.getClass();
        if (sourceClass == Boolean.TYPE || sourceClass == Boolean.class) {
            return (Boolean) fieldValue;
        }
        if (sourceClass == String.class) {
            return Boolean.valueOf(String.valueOf(fieldValue));
        }
        throw new SPANException("Cannot transform type " + fieldValue.getClass() + " to Boolean. " + ThreadContext.get(SPExecutor.MDC_SQL_QUERY) + ". Field: " + ThreadContext.get(SPExecutor.MDC_FIELD));
    }

    /**
     * Transforms input object value to Integer.
     *
     * @param obj           Input Value
     * @return              Transformed Value
     */
    private BigInteger getBigIntegerVal(Object obj) {

        Class sourceClass = obj.getClass();
        if (sourceClass == Integer.class || sourceClass == Integer.TYPE) {
            Integer val = (Integer) obj;
            return BigInteger.valueOf(val.longValue());
        }

        if (sourceClass == Long.class || sourceClass == Long.TYPE) {
            Long val = (Long) obj;
            return BigDecimal.valueOf(val.doubleValue()).toBigInteger();
        }

        if (sourceClass == Float.class || sourceClass == Float.TYPE) {
            logWarn("Converting Float to BigInteger.");
            Float val = (Float) obj;
            return BigDecimal.valueOf(val.doubleValue()).toBigInteger();
        }

        if (sourceClass == Double.class || sourceClass == Double.TYPE) {
            logWarn("Converting Double to BigInteger.");
            Double val = (Double) obj;
            return BigDecimal.valueOf(val.doubleValue()).toBigInteger();
        }

        if (sourceClass == BigInteger.class) {
            return (BigInteger) obj;
        }

        if (sourceClass == BigDecimal.class) {
            logWarn("Converting BigDecimal to BigInteger.");
            BigDecimal val = (BigDecimal) obj;
            return val.toBigInteger();
        }

        if (sourceClass == String.class) {
            return new BigInteger(getStringValFromNumericType(obj));
        }

        throw new SPANException("Conversion from " + sourceClass + " to BigInteger is not supported. " + ThreadContext.get(SPExecutor.MDC_SQL_QUERY) + ". Field: " + ThreadContext.get(SPExecutor.MDC_FIELD));
    }


    /**
     * Transforms input object value to BigDecimal.
     *
     * @param obj           Input Value
     * @return              Transformed Value
     */
    private BigDecimal getBigDecimalVal(Object obj) {

        Class sourceClass = obj.getClass();
        if (sourceClass == Integer.class || sourceClass == Integer.TYPE) {
            Integer val = (Integer) obj;
            return BigDecimal.valueOf(val.doubleValue());
        }

        if (sourceClass == Long.class || sourceClass == Long.TYPE) {
            Long val = (Long) obj;
            return BigDecimal.valueOf(val.doubleValue());
        }

        if (sourceClass == Float.class || sourceClass == Float.TYPE) {
            Float val = (Float) obj;
            return BigDecimal.valueOf(val.floatValue());
        }

        if (sourceClass == Double.class || sourceClass == Double.TYPE) {
            Double val = (Double) obj;
            return BigDecimal.valueOf(val.doubleValue());
        }

        if (sourceClass == BigInteger.class) {
            BigInteger val = (BigInteger) obj;
            return new BigDecimal(val);
        }

        if (sourceClass == BigDecimal.class) {
            return (BigDecimal) obj;
        }

        if (sourceClass == String.class) {
            return new BigDecimal(getStringValFromNumericType(obj));
        }

        throw new SPANException("Conversion from " + sourceClass + " to BigDecimal is not supported. " + ThreadContext.get(SPExecutor.MDC_SQL_QUERY) + ". Field: " + ThreadContext.get(SPExecutor.MDC_FIELD));
    }


    /**
     * Transforms input object value to Double.
     *
     * @param obj           Input Value
     * @return              Transformed Value
     */
    private Double getDoubleVal(Object obj) {
        Class sourceClass = obj.getClass();
        if (sourceClass == Integer.class || sourceClass == Integer.TYPE) {
            Integer val = (Integer) obj;
            return Double.valueOf(val.doubleValue());
        }

        if (sourceClass == Long.class || sourceClass == Long.TYPE) {
            Long val = (Long) obj;
            return Double.valueOf(val.doubleValue());
        }

        if (sourceClass == Float.TYPE || sourceClass == Float.class) {
            Float val = (Float) obj;
            return Double.valueOf(val.doubleValue());
        }

        if (sourceClass == Double.TYPE || sourceClass == Double.class) {
            return (Double) obj;
        }


        if (sourceClass == BigDecimal.class) {
            logWarn("Converting BigDecimal to Double.");
            BigDecimal val = (BigDecimal) obj;
            return Double.valueOf(val.doubleValue());
        }

        if (sourceClass == BigInteger.class) {
            logWarn("Converting BigInteger to Double.");
            BigInteger val = (BigInteger) obj;
            return Double.valueOf(val.doubleValue());
        }

        if (sourceClass == String.class) {
            return Double.valueOf(getStringValFromNumericType(obj));
        }

        throw new SPANException("Conversion from " + sourceClass + " to Double is not supported. " + ThreadContext.get(SPExecutor.MDC_SQL_QUERY) + ". Field: " + ThreadContext.get(SPExecutor.MDC_FIELD));


    }


    /**
     * Transforms input object value to Float.
     *
     * @param obj           Input Value
     * @return              Transformed Value
     */
    private Float getFloatVal(Object obj) {
        Class sourceClass = obj.getClass();
        if (sourceClass == Integer.class || sourceClass == Integer.TYPE) {
            Integer val = (Integer) obj;
            return Float.valueOf(val.floatValue());
        }

        if (sourceClass == Long.class || sourceClass == Long.TYPE) {
            Long val = (Long) obj;
            return Float.valueOf(val.floatValue());
        }

        if (sourceClass == Float.class || sourceClass == Float.TYPE) {
            return (Float) obj;
        }

        if (sourceClass == Double.class || sourceClass == Double.TYPE) {
            logWarn("Converting Double to Float.");
            Double val = (Double) obj;
            return Float.valueOf(val.floatValue());
        }

        if (sourceClass == BigDecimal.class) {
            logWarn("Converting BigDecimal to Float.");
            BigDecimal val = (BigDecimal) obj;
            return Float.valueOf(val.floatValue());
        }

        if (sourceClass == BigInteger.class) {
            logWarn("Converting BigInteger to Float.");
            BigInteger val = (BigInteger) obj;
            return Float.valueOf(val.floatValue());
        }

        if (sourceClass == String.class) {
            return Float.valueOf(getStringValFromNumericType(obj));
        }

        throw new SPANException("Conversion from " + sourceClass + " to Float is not supported. " + ThreadContext.get(SPExecutor.MDC_SQL_QUERY) + ". Field: " + ThreadContext.get(SPExecutor.MDC_FIELD));
    }


    /**
     * Utility method to log warn value for given SQL Query and Field/
     *
     * @param msg
     */
    private void logWarn(final String msg) {
        logger.warn(msg + "/ Transformation may not be safe. Query: {} , Field: {} " , ThreadContext.get(SPExecutor.MDC_SQL_QUERY), ThreadContext.get(SPExecutor.MDC_FIELD));
    }

    /**
     * Transforms input object value to Integer.
     *
     * @param obj           Input Value
     * @return              Transformed Value
     */
    private Integer getIntValue(Object obj) {
        Class sourceClass = obj.getClass();

        if (sourceClass == Integer.class || sourceClass == Integer.TYPE) {
            return (Integer) obj;
        }

        if (sourceClass == Long.class || sourceClass == Long.TYPE) {
            logWarn("Converting Long to Integer.");
            Long val = (Long) obj;
            return Integer.valueOf(val.intValue());
        }

        if (sourceClass == Float.class || sourceClass == Float.TYPE) {
            logWarn("Converting Float to Integer.");
            Float val = (Float) obj;
            return Integer.valueOf(val.intValue());
        }

        if (sourceClass == Double.class || sourceClass == Double.TYPE) {
            logWarn("Converting Double to Integer.");
            Double val = (Double) obj;
            return Integer.valueOf(val.intValue());
        }

        if (sourceClass == BigDecimal.class) {
            logWarn("Converting BigDecimal to Integer.");
            BigDecimal val = (BigDecimal) obj;
            return Integer.valueOf(val.intValue());
        }

        if (sourceClass == BigInteger.class) {
            logWarn("Converting BigInteger to Integer.");
            BigInteger val = (BigInteger) obj;
            return Integer.valueOf(val.intValue());
        }

        if (sourceClass == String.class) {
            return Integer.valueOf(getStringValFromNumericType(obj));
        }

        throw new SPANException("Conversion from " + sourceClass + " to Integer is not supported. " + ThreadContext.get(SPExecutor.MDC_SQL_QUERY) + ". Field: " + ThreadContext.get(SPExecutor.MDC_FIELD));
    }


    /**
     * Utility method to convert input "" to 0.
     *
     * @param obj       String object
     * @return
     */
    private String getStringValFromNumericType(Object obj) {
        String val = String.valueOf(obj);
        if ("".equals(val)) {
            val = "0";
        }
        return val;
    }

    /**
     * Transforms input object value to Long.
     *
     * @param obj    Input Value
     * @return              Transformed Value
     */
    private Long getLongVal(Object obj) {
        Class sourceClass = obj.getClass();

        if (sourceClass == Integer.class || sourceClass == Integer.TYPE) {
            Integer val = (Integer) obj;
            return Long.valueOf(val.longValue());
        }

        if (sourceClass == Long.class || sourceClass == Long.TYPE) {
            return (Long) obj;
        }

        if (sourceClass == Float.class || sourceClass == Float.TYPE) {
            logWarn("Converting Float to Long. " + ThreadContext.get(SPExecutor.MDC_SQL_QUERY) + ". Field: " + ThreadContext.get(SPExecutor.MDC_FIELD));
            Float val = (Float) obj;
            return Long.valueOf(val.longValue());
        }

        if (sourceClass == Double.class || sourceClass == Double.TYPE) {
            logWarn("Converting Double to Long.");
            Double val = (Double) obj;
            return Long.valueOf(val.longValue());
        }

        if (sourceClass == BigDecimal.class) {
            logWarn("Converting BigDecimal to Long.");
            BigDecimal val = (BigDecimal) obj;
            return Long.valueOf(val.longValue());
        }

        if (sourceClass == BigInteger.class) {
            logWarn("Converting BigInteger to Long.");
            BigInteger val = (BigInteger) obj;
            return Long.valueOf(val.longValue());
        }

        if (sourceClass == String.class) {
            return Long.valueOf(getStringValFromNumericType(obj));
        }


        throw new SPANException("Conversion from " + sourceClass + " to Long is not supported. " + ThreadContext.get(SPExecutor.MDC_SQL_QUERY) + ". Field: " + ThreadContext.get(SPExecutor.MDC_FIELD));
    }

}

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
package com.americanexpress.span.core;

import com.americanexpress.span.annotation.Field;
import com.americanexpress.span.annotation.ResultSet;
import com.americanexpress.span.core.database.SPExecutor;
import com.americanexpress.span.exceptions.DuplicateFieldException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

public class SPExecutorTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    /**
     * Nested Class for testing DuplicateFields
     */
    public class DuplicateFieldTestClass {

        @Field(name = "fieldInteger")
        private Integer fieldInteger;

        @Field(name = "fieldInteger")
        private String fieldInteger2;

        @Field(name = "fieldInt")
        private int fieldInt;

    }

    /**
     * Nested Class for testing DuplicateMethodFields
     */
    public class DuplicateMethodFieldTestClass {

        private String fieldString;
        private int fieldInt;

        @Field(name = "fieldInt")
        public String getFieldString() {
            return fieldString;
        }

        @Field(name = "fieldInt")
        public int getFieldInt() {
            return fieldInt;
        }

    }


    /**
     * Nested Class for testing DuplicateMethodFields
     */
    public class DuplicateFieldsInResultSetTestClass {

        private String fieldString;
        private int fieldInt;

        @ResultSet
        private List<TestDuplicateFieldResultSet> fieldResultSet;

        @Field(name = "fieldInt")
        public String getFieldString() {
            return fieldString;
        }

    }


    /**
     * Nested Class for testing DuplicateMethodFields
     */
    public class TestDuplicateFieldResultSet {

        private String fieldString;
        private int fieldInt;

        @Field(name = "fieldInt")
        public String getFieldString() {
            return fieldString;
        }

        @Field(name = "fieldInt")
        public int getFieldInt() {
            return fieldInt;
        }

    }

    @Test
    public void testDuplicateFields() {
        expectedException.expect(DuplicateFieldException.class);
        expectedException.expectMessage("Duplicate fields in "+DuplicateFieldTestClass.class.getSimpleName());
        SPExecutor.validateFields(DuplicateFieldTestClass.class);
    }

    @Test
    public void testDuplicateMethodFields() {
        expectedException.expect(DuplicateFieldException.class);
        SPExecutor.validateFields(DuplicateMethodFieldTestClass.class);
    }

    @Test
    public void testDuplicateFieldsInResultSetTestClass() {
        expectedException.expect(DuplicateFieldException.class);
        SPExecutor.validateFields(DuplicateFieldsInResultSetTestClass.class);
    }
}

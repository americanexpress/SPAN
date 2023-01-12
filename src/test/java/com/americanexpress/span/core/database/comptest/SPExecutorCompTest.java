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
package com.americanexpress.span.core.database.comptest;

import com.americanexpress.span.core.SPANInitialization;
import com.americanexpress.span.core.database.SPExecutor;
import com.americanexpress.span.core.database.comptest.simple.SPInput;
import com.americanexpress.span.core.database.comptest.simple.SPOutput;
import com.americanexpress.span.core.database.comptest.transformationclass.RangeEnum;
import com.americanexpress.span.core.database.connection.SPANDataSource;
import com.americanexpress.span.core.database.handlers.impl.InputHandlerImpl;
import com.americanexpress.span.core.database.handlers.impl.OutputHandlerImpl;
import com.americanexpress.span.core.database.handlers.impl.ResultSetHandlerImpl;
import com.americanexpress.span.exceptions.SPANException;
import com.americanexpress.span.utility.PropertyConfiguration;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.americanexpress.span.core.SPANConfigHolderTest.resetHoldSPANConfigForTesting;
import static org.junit.Assert.*;

public class SPExecutorCompTest {

    private static final String COMP_TEST_USER_DEF_KEY = "COMP-TEST-SP-KEY";
    private static final String COMP_TEST_NORS_USER_DEF_KEY = "COMP-TEST-NO-RS-SP-KEY";
    private static final String MULTI_RS_USER_DEF_KEY = "MULTIRS-KEY";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @BeforeClass
    public static void beforeClass() throws IOException, ClassNotFoundException, SQLException, NoSuchFieldException, IllegalAccessException {
        System.out.println("SPExecutorCompTest is started");
        resetHoldSPANConfigForTesting();

        SPANInitialization.initialize(new PropertyConfiguration() {
            public String getSPANConfigFileName(){
                return "SPANConfig-hsqldb.yaml";
           }
        });
        StringBuffer sbFileContent = new StringBuffer();
        try (BufferedReader br =
                     new BufferedReader(new InputStreamReader(SPExecutorCompTest.class.getClassLoader().getResourceAsStream("component-test-setup.sql")))) {
            String line;
            while ((line =  br.readLine()) != null) {
                sbFileContent.append(line).append(System.getProperty("line.separator"));
            }
        }
        String fileContent = sbFileContent.toString();

        try (Connection conn = SPANDataSource.getInstance().getDataSource(COMP_TEST_USER_DEF_KEY).getConnection()) {
            Arrays.asList(fileContent.split("---")).stream().forEach(s -> {
                System.out.println("Executing Statement:" + s);
                try {
                    conn.prepareStatement(s).execute();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });

        }
    }


    @Test
    /**
     * This test executes new SPExecutor().execute() method for the Stored Procedure present in component-test-setup.sql.
     * The Stored Procedure returns output variables by copying the data from input variables.
     */
    public void testSimple() throws SQLException {
        SPInput inputObject = createInputClass();

        SPOutput outputObject = new SPExecutor().execute(COMP_TEST_USER_DEF_KEY, inputObject, SPOutput.class);

        validateOutputClass(inputObject, outputObject);

    }

    private void validateOutputClass(SPInput inputObject, SPOutput outputObject) {
        assertEquals(inputObject.getInString(), outputObject.getOutString());
        assertEquals(inputObject.getInInt(), outputObject.getOutInt());
        assertEquals(inputObject.getInInteger(), outputObject.getOutInteger());
        assertEquals(inputObject.getInLong(), outputObject.getOutLong());
        assertEquals(inputObject.getInLongObj(), outputObject.getOutLongObj());


        assertTrue(inputObject.getInFloat() + " doesn't match " + outputObject.getOutFloat(),
                inputObject.getInFloat() == outputObject.getOutFloat());
        assertEquals(inputObject.getInFloatObj(), outputObject.getOutFloatObj());

        assertTrue(inputObject.getInDouble() + " doesn't match " + outputObject.getOutDouble(),
                inputObject.getInDouble() == outputObject.getOutDouble());
        assertEquals(inputObject.getInDoubleObj(), outputObject.getOutDoubleObj());

        assertEquals(inputObject.getInBigInteger(), outputObject.getOutBigInteger());
        assertEquals(0, inputObject.getInDecimal().compareTo(outputObject.getOutDecimal()));
        assertEquals(inputObject.isInBoolean(), outputObject.isOutBoolean());
        assertEquals(inputObject.getInBooleanObj(), outputObject.getOutBooleanObj());

        assertEquals(2, outputObject.getListRSObj().size());
        assertEquals(inputObject.getInTimestamp().truncatedTo(ChronoUnit.SECONDS), outputObject.getOutTimestamp().truncatedTo(ChronoUnit.SECONDS));

        AtomicBoolean foundRequiredRow = new AtomicBoolean(false);
        outputObject.getListRSObj().stream().forEach(resultSetClass -> {
            if (resultSetClass.getRsString().equals("2-RS")) {
                foundRequiredRow.set(true);
                assertEquals(2, resultSetClass.getRsInt());
                assertEquals(Integer.valueOf(20), resultSetClass.getRsInteger());


                assertTrue("200 doesn't match " + resultSetClass.getRsFloat(),
                        200 == resultSetClass.getRsFloat());
                assertEquals(Float.valueOf(2000), resultSetClass.getRsFloatObj());

                assertTrue("20000 doesn't match " + resultSetClass.getRsDouble(),
                        20000 == resultSetClass.getRsDouble());
                assertEquals(Double.valueOf(200000), resultSetClass.getRsDoubleObj());

                assertEquals(BigInteger.valueOf(2000000), resultSetClass.getRsBigInteger());
                assertEquals(0, BigDecimal.valueOf(20000000).compareTo(resultSetClass.getRsDecimal()));
                assertEquals(true, resultSetClass.isRsBoolean());
                assertEquals(true, resultSetClass.getRsBooleanObj());

                assertEquals(400, resultSetClass.getRsLong());
                assertEquals(Long.valueOf(4000), resultSetClass.getRsLongObj());

           }
        });

        if (!foundRequiredRow.get()) {
            fail("Couldn't find resultset with rs_string: 2-RS");
        }
    }


    @Test
    /**
     * This test executes new SPExecutor().execute() method for the Stored Procedure present in component-test-setup.sql.
     * The Stored Procedure returns output variables by copying the data from input variables.
     */
    public void testGetMethod() throws SQLException {
        com.americanexpress.span.core.database.comptest.getmethod.SPInput inputObject = new com.americanexpress.span.core.database.comptest.getmethod.SPInput();
        inputObject.setInString("ABC");
        inputObject.setInInt(100);
        inputObject.setInInteger(1000);
        inputObject.setInLong(1_000_000);
        inputObject.setInLongObj(10_000_000L);
        inputObject.setInFloat(10_000F);
        inputObject.setInFloatObj(100_000F);
        inputObject.setInDouble(10_000_000.0D);
        inputObject.setInDoubleObj(1_000_000.0D);
        inputObject.setInBigInteger(BigInteger.valueOf(100_000_000));
        inputObject.setInDecimal(BigDecimal.valueOf(1000_000_000.0D));
        inputObject.setInBoolean(true);
        inputObject.setInBooleanObj(true);
        inputObject.setInTimestamp(LocalDateTime.now());

        com.americanexpress.span.core.database.comptest.getmethod.SPOutput outputObject =
                new SPExecutor().execute(COMP_TEST_USER_DEF_KEY, inputObject, com.americanexpress.span.core.database.comptest.getmethod.SPOutput.class);

        assertEquals(inputObject.getInString(), outputObject.getOutString());
        assertEquals(inputObject.getInInt(), outputObject.getOutInt());
        assertEquals(inputObject.getInInteger(), outputObject.getOutInteger());
        assertEquals(inputObject.getInLong(), outputObject.getOutLong());
        assertEquals(inputObject.getInLongObj(), outputObject.getOutLongObj());


        assertTrue(inputObject.getInFloat() + " doesn't match " + outputObject.getOutFloat(),
                inputObject.getInFloat() == outputObject.getOutFloat());
        assertEquals(inputObject.getInFloatObj(), outputObject.getOutFloatObj());

        assertTrue(inputObject.getInDouble() + " doesn't match " + outputObject.getOutDouble(),
                inputObject.getInDouble() == outputObject.getOutDouble());
        assertEquals(inputObject.getInDoubleObj(), outputObject.getOutDoubleObj());

        assertEquals(inputObject.getInBigInteger(), outputObject.getOutBigInteger());
        assertEquals(0, inputObject.getInDecimal().compareTo(outputObject.getOutDecimal()));
        assertEquals(inputObject.isInBoolean(), outputObject.isOutBoolean());
        assertEquals(inputObject.getInBooleanObj(), outputObject.getOutBooleanObj());

        assertEquals(2, outputObject.getListRSObj().size());
        assertEquals(inputObject.getInTimestamp().truncatedTo(ChronoUnit.SECONDS), outputObject.getOutTimestamp().truncatedTo(ChronoUnit.SECONDS));


        AtomicBoolean foundRequiredRow = new AtomicBoolean(false);
        outputObject.getListRSObj().stream().forEach(resultSetClass -> {
            if (resultSetClass.getRsString().equals("2-RS")) {
                foundRequiredRow.set(true);
                assertEquals(2, resultSetClass.getRsInt());
                assertEquals(Integer.valueOf(20), resultSetClass.getRsInteger());


                assertTrue("200 doesn't match " + resultSetClass.getRsFloat(),
                        200 == resultSetClass.getRsFloat());
                assertEquals(Float.valueOf(2000), resultSetClass.getRsFloatObj());

                assertTrue("20000 doesn't match " + resultSetClass.getRsDouble(),
                        20000 == resultSetClass.getRsDouble());
                assertEquals(Double.valueOf(200000), resultSetClass.getRsDoubleObj());

                assertEquals(BigInteger.valueOf(2000000), resultSetClass.getRsBigInteger());
                assertEquals(0, BigDecimal.valueOf(20000000).compareTo(resultSetClass.getRsDecimal()));
                assertEquals(true, resultSetClass.isRsBoolean());
                assertEquals(true, resultSetClass.getRsBooleanObj());

                assertEquals(400, resultSetClass.getRsLong());
                assertEquals(Long.valueOf(4000), resultSetClass.getRsLongObj());

           }
        });

        if (!foundRequiredRow.get()) {
            fail("Couldn't find resultset with rs_string: 2-RS");
        }

    }


    @Test
    /**
     * This test executes new SPExecutor().execute() method for the Stored Procedure present in component-test-setup.sql.
     * The Stored Procedure returns output variables by copying the data from input variables.
     */
    public void testSetMethod() throws SQLException {
        com.americanexpress.span.core.database.comptest.setmethod.SPInput inputObject = new com.americanexpress.span.core.database.comptest.setmethod.SPInput();
        inputObject.setInString("ABC");
        inputObject.setInInt(100);
        inputObject.setInInteger(1000);
        inputObject.setInLong(1_000_000);
        inputObject.setInLongObj(10_000_000L);
        inputObject.setInFloat(10_000F);
        inputObject.setInFloatObj(100_000F);
        inputObject.setInDouble(10_000_000.0D);
        inputObject.setInDoubleObj(1_000_000.0D);
        inputObject.setInBigInteger(BigInteger.valueOf(100_000_000));
        inputObject.setInDecimal(BigDecimal.valueOf(1000_000_000.0D));
        inputObject.setInBoolean(true);
        inputObject.setInBooleanObj(true);
        inputObject.setInTimestamp(LocalDateTime.now());

        com.americanexpress.span.core.database.comptest.setmethod.SPOutput outputObject =
                new SPExecutor().execute(COMP_TEST_USER_DEF_KEY, inputObject, com.americanexpress.span.core.database.comptest.setmethod.SPOutput.class);

        assertEquals(inputObject.getInString(), outputObject.getOutString());
        assertEquals(inputObject.getInInt(), outputObject.getOutInt());
        assertEquals(inputObject.getInInteger(), outputObject.getOutInteger());
        assertEquals(inputObject.getInLong(), outputObject.getOutLong());
        assertEquals(inputObject.getInLongObj(), outputObject.getOutLongObj());


        assertTrue(inputObject.getInFloat() + " doesn't match " + outputObject.getOutFloat(),
                inputObject.getInFloat() == outputObject.getOutFloat());
        assertEquals(inputObject.getInFloatObj(), outputObject.getOutFloatObj());

        assertTrue(inputObject.getInDouble() + " doesn't match " + outputObject.getOutDouble(),
                inputObject.getInDouble() == outputObject.getOutDouble());
        assertEquals(inputObject.getInDoubleObj(), outputObject.getOutDoubleObj());

        assertEquals(inputObject.getInBigInteger(), outputObject.getOutBigInteger());
        assertEquals(0, inputObject.getInDecimal().compareTo(outputObject.getOutDecimal()));
        assertEquals(inputObject.isInBoolean(), outputObject.isOutBoolean());
        assertEquals(inputObject.getInBooleanObj(), outputObject.getOutBooleanObj());

        assertEquals(2, outputObject.getListRSObj().size());
        assertEquals(inputObject.getInTimestamp().truncatedTo(ChronoUnit.SECONDS), outputObject.getOutTimestamp().truncatedTo(ChronoUnit.SECONDS));


        AtomicBoolean foundRequiredRow = new AtomicBoolean(false);
        outputObject.getListRSObj().stream().forEach(resultSetClass -> {
            if (resultSetClass.getRsString().equals("2-RS")) {
                foundRequiredRow.set(true);
                assertEquals(2, resultSetClass.getRsInt());
                assertEquals(Integer.valueOf(20), resultSetClass.getRsInteger());


                assertTrue("200 doesn't match " + resultSetClass.getRsFloat(),
                        200 == resultSetClass.getRsFloat());
                assertEquals(Float.valueOf(2000), resultSetClass.getRsFloatObj());

                assertTrue("20000 doesn't match " + resultSetClass.getRsDouble(),
                        20000 == resultSetClass.getRsDouble());
                assertEquals(Double.valueOf(200000), resultSetClass.getRsDoubleObj());

                assertEquals(BigInteger.valueOf(2000000), resultSetClass.getRsBigInteger());
                assertEquals(0, BigDecimal.valueOf(20000000).compareTo(resultSetClass.getRsDecimal()));
                assertEquals(true, resultSetClass.isRsBoolean());
                assertEquals(true, resultSetClass.getRsBooleanObj());

                assertEquals(400, resultSetClass.getRsLong());
                assertEquals(Long.valueOf(4000), resultSetClass.getRsLongObj());

           }
        });

        if (!foundRequiredRow.get()) {
            fail("Couldn't find resultset with rs_string: 2-RS");
        }

    }


    @Test
    /**
     * This test executes new SPExecutor().execute() method for the Stored Procedure present in component-test-setup.sql.
     * The Stored Procedure returns output variables by copying the data from input variables.
     */
    public void testMissingRSAnnotation() throws SQLException {
        com.americanexpress.span.core.database.comptest.missingrsannotation.SPInput inputObject = new com.americanexpress.span.core.database.comptest.missingrsannotation.SPInput();
        inputObject.setInString("ABC");
        inputObject.setInInt(100);
        inputObject.setInInteger(1000);
        inputObject.setInLong(1_000_000);
        inputObject.setInLongObj(10_000_000L);
        inputObject.setInFloat(10_000F);
        inputObject.setInFloatObj(100_000F);
        inputObject.setInDouble(10_000_000.0D);
        inputObject.setInDoubleObj(1_000_000.0D);
        inputObject.setInBigInteger(BigInteger.valueOf(100_000_000));
        inputObject.setInDecimal(BigDecimal.valueOf(1000_000_000.0D));
        inputObject.setInBoolean(true);
        inputObject.setInBooleanObj(true);
        inputObject.setInTimestamp(LocalDateTime.now());

        com.americanexpress.span.core.database.comptest.missingrsannotation.SPOutput outputObject =
                new SPExecutor().execute(COMP_TEST_USER_DEF_KEY, inputObject, com.americanexpress.span.core.database.comptest.missingrsannotation.SPOutput.class);

        assertNull(outputObject.getListRSObj());


    }


    @Test
    public void testNoResultSet() throws SQLException {

        expectedException.expect(SPANException.class);
        expectedException.expectMessage("Stored Procedure didn't return enough resultSets. Expected: 1. Actual: 0. {call SCHEMA_ID.NORESULTSET()}");

        com.americanexpress.span.core.database.comptest.noresultset.SPInput inputObject = new com.americanexpress.span.core.database.comptest.noresultset.SPInput();
        new SPExecutor().execute(COMP_TEST_NORS_USER_DEF_KEY, inputObject, com.americanexpress.span.core.database.comptest.noresultset.SPOutput.class);


    }

    @Test
    public void testInvalidRSType() throws SQLException {

        expectedException.expect(SPANException.class);
        expectedException.expectMessage("ResultSet Field should be a List.");

        com.americanexpress.span.core.database.comptest.invalidrstype.SPInput inputObject = new com.americanexpress.span.core.database.comptest.invalidrstype.SPInput();
        new SPExecutor().execute(MULTI_RS_USER_DEF_KEY, inputObject, com.americanexpress.span.core.database.comptest.invalidrstype.SPOutput.class);


    }


    @Test
    public void testInvalidParams() throws SQLException {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage("SPUser Defined Key: COMP-TEST-SP-KEY. Input Object: null. ReturnType: null cannot be null.");

        new SPExecutor().execute(COMP_TEST_USER_DEF_KEY, null, null);
    }


    @Test
    public void testInvalidSPUserKey() throws SQLException {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage("Invalid SPUserDefined Key. Datasource cannot be null. INVALID-KEY");

        new SPExecutor().execute("INVALID-KEY", new SPInput(), SPOutput.class);
    }

    @Test
    public void testDuplicateRSSeqNum() throws SQLException {
        expectedException.expect(SPANException.class);
        expectedException.expectMessage("@ResultSet should have unique continuous Sequence numbers starting at 1.");

        new SPExecutor().execute(MULTI_RS_USER_DEF_KEY, new com.americanexpress.span.core.database.comptest.duplicatersseqnum.SPInput(),
                com.americanexpress.span.core.database.comptest.duplicatersseqnum.SPOutput.class);
    }

    @Test
    public void testRSNotInSeq() throws SQLException {
        expectedException.expect(SPANException.class);
        expectedException.expectMessage("@ResultSet should have unique Sequence numbers starting at 1. [2, 3, 4].");

        new SPExecutor().execute(MULTI_RS_USER_DEF_KEY, new com.americanexpress.span.core.database.comptest.rsnotinseq.SPInput(),
                com.americanexpress.span.core.database.comptest.rsnotinseq.SPOutput.class);
    }

    @Test
    public void testRSNotInSeqLast() throws SQLException {
        expectedException.expect(SPANException.class);
        expectedException.expectMessage("@ResultSet should have unique Sequence numbers starting at 1. [1, 2, 4].");

        new SPExecutor().execute(MULTI_RS_USER_DEF_KEY, new com.americanexpress.span.core.database.comptest.rsnotinseqlast.SPInput(),
                com.americanexpress.span.core.database.comptest.rsnotinseqlast.SPOutput.class);
    }

    //Test
    /**
     * This test executes new SPExecutor().execute() method for the Stored Procedure present in component-test-setup.sql.
     * The Stored Procedure returns output variables by copying the data from input variables.
     */
    public void testSimpleAllArgConstructor() throws SQLException {
        SPInput inputObject = createInputClass();

        SPOutput outputObject = new SPExecutor(new InputHandlerImpl(), new OutputHandlerImpl(), new ResultSetHandlerImpl())
                .execute(COMP_TEST_USER_DEF_KEY, inputObject, SPOutput.class);

        validateOutputClass(inputObject, outputObject);
    }


    @Test
    /**
     * This test executes new SPExecutor().execute() method for the Stored Procedure present in component-test-setup.sql.
     * The Stored Procedure returns output variables by copying the data from input variables.
     */
    public void testSimpleHandlerSetters() throws SQLException {
        SPInput inputObject = createInputClass();

        SPExecutor spExecutor = new SPExecutor();
        spExecutor.setInputHandler(new InputHandlerImpl());
        spExecutor.setOutputHandler(new OutputHandlerImpl());
        spExecutor.setResultSetHandler(new ResultSetHandlerImpl());
        SPOutput outputObject = spExecutor
                .execute(COMP_TEST_USER_DEF_KEY, inputObject, SPOutput.class);

        validateOutputClass(inputObject, outputObject);

    }

    @Test
    /**
     * This test executes new SPExecutor().execute() method for the Stored Procedure present in component-test-setup.sql.
     * The Stored Procedure returns output variables by copying the data from input variables.
     */
    public void testMultiRS() throws SQLException {
        com.americanexpress.span.core.database.comptest.multirs.SPInput inputObject = new com.americanexpress.span.core.database.comptest.multirs.SPInput();

        SPExecutor spExecutor = new SPExecutor();
        spExecutor.setInputHandler(new InputHandlerImpl());
        spExecutor.setOutputHandler(new OutputHandlerImpl());
        spExecutor.setResultSetHandler(new ResultSetHandlerImpl());
        com.americanexpress.span.core.database.comptest.multirs.SPOutput outputObject = spExecutor
                .execute(MULTI_RS_USER_DEF_KEY, inputObject, com.americanexpress.span.core.database.comptest.multirs.SPOutput.class);

        assertNotNull(outputObject.getListRSObj());
        assertNotNull(outputObject.getListRSObj2());
        assertNotNull(outputObject.getListRSObj3());

    }


    private SPInput createInputClass() {
        SPInput inputObject = new SPInput();
        inputObject.setInString("ABC");
        inputObject.setInInt(100);
        inputObject.setInInteger(1000);
        inputObject.setInLong(1_000_000);
        inputObject.setInLongObj(10_000_000L);
        inputObject.setInFloat(10_000F);
        inputObject.setInFloatObj(100_000F);
        inputObject.setInDouble(10_000_000.0D);
        inputObject.setInDoubleObj(1_000_000.0D);
        inputObject.setInBigInteger(BigInteger.valueOf(100_000_000));
        inputObject.setInDecimal(BigDecimal.valueOf(1000_000_000.0D));
        inputObject.setInBoolean(true);
        inputObject.setInBooleanObj(true);
        inputObject.setInTimestamp(LocalDateTime.now());
        return inputObject;
    }

    @Test
    /**
     * This test executes new SPExecutor().execute() method for the Stored Procedure present in component-test-setup.sql.
     * The Stored Procedure returns output variables by copying the data from input variables.
     */
    public void testTransform() throws SQLException {
        com.americanexpress.span.core.database.comptest.transformationclass.SPInput inputObject = new com.americanexpress.span.core.database.comptest.transformationclass.SPInput();
        inputObject.setInString(Boolean.TRUE);
        inputObject.setInBoolean(Boolean.TRUE);
        inputObject.setInBooleanObj(Boolean.FALSE);

        inputObject.setInTimestamp(LocalDateTime.now());

        com.americanexpress.span.core.database.comptest.transformationclass.SPOutput outputObject =
                new SPExecutor().execute(COMP_TEST_USER_DEF_KEY, inputObject, com.americanexpress.span.core.database.comptest.transformationclass.SPOutput.class);

        assertEquals("Yes", outputObject.getOutString());
        assertEquals("No", outputObject.getOutBooleanObj());
        assertEquals("Yes", outputObject.getOutBoolean());

        AtomicBoolean foundRequiredRow = new AtomicBoolean(false);
        outputObject.getListRSObj().stream().forEach(resultSetClass -> {
            if (resultSetClass.getRsString().equals("2-RS")) {
                foundRequiredRow.set(true);
                Assert.assertEquals(RangeEnum.LOW, resultSetClass.getRsInt());
                assertEquals(RangeEnum.HIGH, resultSetClass.getRsInteger());
           }
        });

        if (!foundRequiredRow.get()) {
            fail("Couldn't find resultset with rs_string: 2-RS");
        }

    }

}
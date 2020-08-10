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

import com.americanexpress.span.exceptions.ConfigurationSPANException;
import com.americanexpress.span.exceptions.SPANException;
import com.americanexpress.span.models.SPANConfig;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.EnvironmentVariables;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SPANInitializationTest {


    @Rule
    public final EnvironmentVariables environmentVariables = new EnvironmentVariables();

    @Test
    public void testInitializeSPANConfig() throws Exception {
        SPANConfigHolderTest.resetHoldSPANConfigForTesting();
        SPANInitialization.initialize("SampleSPANConfig.yaml");

        SPANConfig spanConfig = SPANConfigHolder.getInstance().getSPANConfig();

        assertEquals(2,spanConfig.getSpanUserDefineKeys().size());
        assertEquals("DB1",spanConfig.getSpanUserDefineKeys().get("SPAN-DB_ID_1").getDataSourceDetails().getDatabase());
        assertEquals("DB1",spanConfig.getSpanUserDefineKeys().get("SPAN-DB_ID_2").getDataSourceDetails().getDatabase());
        assertNull(spanConfig.getSpanUserDefineKeys().get("SPAN-DB_ID_2").getDataSourceDetails().getValidationQuery());
        assertEquals(8,spanConfig.getSpanUserDefineKeys().get("SPAN-DB_ID_2").getDataSourceDetails().getMaxIdle());
        assertEquals(8,spanConfig.getSpanUserDefineKeys().get("SPAN-DB_ID_2").getDataSourceDetails().getMinIdle());
        assertEquals(10,spanConfig.getSpanUserDefineKeys().get("SPAN-DB_ID_2").getDataSourceDetails().getInitialSize());
        assertEquals(1028,spanConfig.getSpanUserDefineKeys().get("SPAN-DB_ID_2").getDataSourceDetails().getMaxActive());
        assertEquals(2000,spanConfig.getSpanUserDefineKeys().get("SPAN-DB_ID_2").getDataSourceDetails().getMaxWaitForConnection());
        assertEquals(10000,spanConfig.getSpanUserDefineKeys().get("SPAN-DB_ID_2").getDataSourceDetails().getTimeBetweenEvictionRunsMillis());
        assertEquals(5,spanConfig.getSpanUserDefineKeys().get("SPAN-DB_ID_2").getDataSourceDetails().getValidationQueryTimeout());


    }

    @Test
    public void testInitializeSPANConfigWithDatabaseProperties() throws Exception {
        SPANConfigHolderTest.resetHoldSPANConfigForTesting();
        SPANInitialization.initialize("SampleSPANConfigWithDatabaseProperties.yaml");

        SPANConfig spanConfig = SPANConfigHolder.getInstance().getSPANConfig();

        assertEquals("select 1",spanConfig.getSpanUserDefineKeys().get("SPAN-EMPLOYEE").getDataSourceDetails().getValidationQuery());
        assertEquals(10,spanConfig.getSpanUserDefineKeys().get("SPAN-EMPLOYEE").getDataSourceDetails().getMaxIdle());
        assertEquals(10,spanConfig.getSpanUserDefineKeys().get("SPAN-EMPLOYEE").getDataSourceDetails().getMinIdle());
        assertEquals(20,spanConfig.getSpanUserDefineKeys().get("SPAN-EMPLOYEE").getDataSourceDetails().getInitialSize());
        assertEquals(1000,spanConfig.getSpanUserDefineKeys().get("SPAN-EMPLOYEE").getDataSourceDetails().getMaxActive());
        assertEquals(3000,spanConfig.getSpanUserDefineKeys().get("SPAN-EMPLOYEE").getDataSourceDetails().getMaxWaitForConnection());
        assertEquals(20000,spanConfig.getSpanUserDefineKeys().get("SPAN-EMPLOYEE").getDataSourceDetails().getTimeBetweenEvictionRunsMillis());
        assertEquals(10,spanConfig.getSpanUserDefineKeys().get("SPAN-EMPLOYEE").getDataSourceDetails().getValidationQueryTimeout());

    }

    @Test(expected = SPANException.class)
    public void testInitializeInvalidSPANConfig() throws Exception {
        SPANConfigHolderTest.resetHoldSPANConfigForTesting();
        SPANInitialization.initialize("InvalidSPANConfig.yaml");

    }

    @Test(expected = Exception.class)
    public void testInitializeNoFileSPANConfig() throws Exception {
        SPANConfigHolderTest.resetHoldSPANConfigForTesting();
        SPANInitialization.initialize("NoFileSPANConfig.yaml");

    }

    @Test(expected = ConfigurationSPANException.class)
    public void testInitializeSPANConfigVailidationFailure() throws Exception {
        SPANConfigHolderTest.resetHoldSPANConfigForTesting();
        SPANInitialization.initialize("SPANConfigNoHostName.yaml");

    }

    @Test
    public void testInitializeDuplicateSPANUserKeySPANConfig() throws Exception {
        SPANConfigHolderTest.resetHoldSPANConfigForTesting();
        SPANInitialization.initialize("DuplicateSPANUserKeyConfig.yaml");

    }

    @Test
    public void testInitializeDuplicateSPUserKeySPANConfig() throws Exception {
        SPANConfigHolderTest.resetHoldSPANConfigForTesting();
        SPANInitialization.initialize("DuplicateSPUserKeyConfig.yaml");

    }

    @Test
    public void testInitializeSPANConfigNoEnv() throws Exception {
        SPANConfigHolderTest.resetHoldSPANConfigForTesting();


        SPANInitialization.initialize(null);

        SPANConfig spanConfig = SPANConfigHolder.getInstance().getSPANConfig();

        assertEquals(2,spanConfig.getSpanUserDefineKeys().size());
        assertEquals("DB1",spanConfig.getSpanUserDefineKeys().get("SPAN-DB_ID_1").getDataSourceDetails().getDatabase());

    }


    @Test
    public void testInitializeSPANConfigWithProperties() throws Exception {
        SPANConfigHolderTest.resetHoldSPANConfigForTesting();
        SPANInitialization.initialize("SampleSPANConfigWithProperties.yaml");

        SPANConfig spanConfig = SPANConfigHolder.getInstance().getSPANConfig();

        assertEquals(2,spanConfig.getSpanUserDefineKeys().size());
        assertEquals("DB1",spanConfig.getSpanUserDefineKeys().get("SPAN-DB_ID_1").getDataSourceDetails().getDatabase());
        assertEquals("DB1",spanConfig.getSpanUserDefineKeys().get("SPAN-DB_ID_2").getDataSourceDetails().getDatabase());
        assertNull(spanConfig.getSpanUserDefineKeys().get("SPAN-DB_ID_2").getDataSourceDetails().getValidationQuery());
        assertEquals(8,spanConfig.getSpanUserDefineKeys().get("SPAN-DB_ID_2").getDataSourceDetails().getMaxIdle());
        assertEquals(8,spanConfig.getSpanUserDefineKeys().get("SPAN-DB_ID_2").getDataSourceDetails().getMinIdle());
        assertEquals(10,spanConfig.getSpanUserDefineKeys().get("SPAN-DB_ID_2").getDataSourceDetails().getInitialSize());
        assertEquals(1028,spanConfig.getSpanUserDefineKeys().get("SPAN-DB_ID_2").getDataSourceDetails().getMaxActive());
        assertEquals(2000,spanConfig.getSpanUserDefineKeys().get("SPAN-DB_ID_2").getDataSourceDetails().getMaxWaitForConnection());
        assertEquals(10000,spanConfig.getSpanUserDefineKeys().get("SPAN-DB_ID_2").getDataSourceDetails().getTimeBetweenEvictionRunsMillis());
        assertEquals(5,spanConfig.getSpanUserDefineKeys().get("SPAN-DB_ID_2").getDataSourceDetails().getValidationQueryTimeout());
        assertEquals("${databasepassword}",spanConfig.getSpanUserDefineKeys().get("SPAN-DB_ID_1").getDataSourceDetails().getPassword());
        assertEquals("${databasepassword}",spanConfig.getSpanUserDefineKeys().get("SPAN-DB_ID_2").getDataSourceDetails().getPassword());

    }

    @Test(expected = ConfigurationSPANException.class)
    public void testInitializeSPANConfigWithWrongPropertiesPath() throws Exception {
        SPANConfigHolderTest.resetHoldSPANConfigForTesting();
        SPANInitialization.initialize("SampleSPANConfigWithWrongPropertiesPath.yaml.yaml");

        SPANConfig spanConfig = SPANConfigHolder.getInstance().getSPANConfig();

    }

}

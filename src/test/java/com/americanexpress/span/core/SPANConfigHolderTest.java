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

import com.americanexpress.span.models.DataSourceDetails;
import com.americanexpress.span.models.SPANConfig;
import com.americanexpress.span.models.SPANUserDefineKeyDetails;
import com.americanexpress.span.models.SPUserDefineKey;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.Assert.*;

public class SPANConfigHolderTest {


    @Test
    public void testSPANConfigLoader() throws Exception {
        resetHoldSPANConfigForTesting();
        SPANInitialization.initialize("SPANConfig.yaml");

        SPANConfig spanConfig = SPANConfigHolder.getInstance().getSPANConfig();

        assertEquals(2,spanConfig.getSpanUserDefineKeys().size());
        assertEquals("DB1",spanConfig.getSpanUserDefineKeys().get("SPAN-DB_ID_1").getDataSourceDetails().getDatabase() );
        assertEquals(2,spanConfig.getSpanUserDefineKeys().get("SPAN-DB_ID_1").getSpUserDefineKeys().size());
        assertEquals("PROC_NAME_1",spanConfig.getSpanUserDefineKeys().get("SPAN-DB_ID_1").getSpUserDefineKeys().get("PROC_ID_4").getProcedure());
        assertNotNull(spanConfig.toString());
    }

    @Test
    public void testSPANConfigHolderDataSource() throws Exception {
        resetHoldSPANConfigForTesting();
        SPANInitialization.initialize("SPANConfig.yaml");

        DataSourceDetails dataSourceDetails = SPANConfigHolder.getInstance().getDataSourceDetails("PROC_ID_1");

        assertEquals("example.com", dataSourceDetails.getHostName());
        assertEquals("7320", dataSourceDetails.getPort());
        assertEquals("DB1", dataSourceDetails.getDatabase());
        assertNotNull(dataSourceDetails.toString());

    }


    @Test
    public void testSPANConfigHolderSPUserName() throws Exception {

        resetHoldSPANConfigForTesting();
        SPANInitialization.initialize("SPANConfig.yaml");

        SPUserDefineKey spUserDefinedKey = SPANConfigHolder.getInstance().getSPUserDefinedKey("PROC_ID_1");

        assertEquals("SCHEMA_ID_2", spUserDefinedKey.getSchema());
        assertEquals("PROC_NAME_3", spUserDefinedKey.getProcedure());
        assertNotNull(spUserDefinedKey.toString());
        assertTrue(spUserDefinedKey.equals(spUserDefinedKey));

    }

    @Test
    public void testSPDetails() throws Exception {

        resetHoldSPANConfigForTesting();
        SPANInitialization.initialize("SPANConfig.yaml");

        SPUserDefineKey spUserDefinedKey = SPANConfigHolder.getInstance().getSPUserDefinedKey("PROC_ID_1");

        assertEquals("SCHEMA_ID_2", spUserDefinedKey.getSchema());
        assertEquals("PROC_NAME_3", spUserDefinedKey.getProcedure());
        assertNotNull(spUserDefinedKey.toString());
        assertTrue(spUserDefinedKey.equals(spUserDefinedKey));

    }

    @Test
    public void testSPANUserDefineKeyDetails() throws Exception {

        resetHoldSPANConfigForTesting();
        SPANInitialization.initialize("SPANConfig.yaml");

        Map<String, SPANUserDefineKeyDetails> spUserDefinedKey = SPANConfigHolder.getInstance().getSPANConfig().getSpanUserDefineKeys();

        assertNotNull( spUserDefinedKey.get("SPAN-DB_ID_1"));
        assertNotNull( spUserDefinedKey.get("SPAN-DB_ID_1").toString());


    }



    public static void resetHoldSPANConfigForTesting() throws ClassNotFoundException, IllegalAccessException, NoSuchFieldException {
        Field field = Class.forName("com.americanexpress.span.core.SPANConfigHolder$HoldSPANConfig").getDeclaredField("holdSPANConfig");
        field.setAccessible(true);
        field.set(null, null);

        Field objectInstance = Class.forName("com.americanexpress.span.core.database.connection.SPANDataSource").getDeclaredField("objectInstance");
        objectInstance.setAccessible(true);
        objectInstance.set(null, null);

    }


}

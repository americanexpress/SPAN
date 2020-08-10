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
package com.americanexpress.span.core.database.connection;

import com.americanexpress.span.core.SPANConfigHolder;
import com.americanexpress.span.core.SPANInitialization;
import com.americanexpress.span.models.SPANConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static com.americanexpress.span.core.SPANConfigHolderTest.resetHoldSPANConfigForTesting;

public class SPANDataSourceTest {

    private SPANConfig expectedOutput() throws IOException {
        SPANConfig spanConfig = null;
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("SPANConfig.yaml")) {
            spanConfig = new ObjectMapper(new YAMLFactory()).enable(DeserializationFeature.UNWRAP_ROOT_VALUE).readValue(inputStream, SPANConfig.class);
        }

        return spanConfig;
    }

    @Test
    public void testInitialize() throws Exception {
        resetHoldSPANConfigForTesting();

        SPANInitialization.initialize("SPANConfig.yaml");
        SPANConfig actualOutput = SPANConfigHolder.getInstance().getSPANConfig();
        SPANConfig expectedOutput = expectedOutput();

        ObjectMapper objectMapper = new ObjectMapper();

        Assert.assertEquals(objectMapper.writeValueAsString(expectedOutput), objectMapper.writeValueAsString(actualOutput));

    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitializeWithWrongDriverClass() throws Exception {
        resetHoldSPANConfigForTesting();

        SPANInitialization.initialize("SPANConfigWithWrongDriverClass.yaml");
        SPANDataSource.getInstance();

    }

    @Test()
    public void testInitializeWithDriverClass() throws Exception {
        resetHoldSPANConfigForTesting();

        SPANInitialization.initialize("SPANConfigWithDriverClass.yaml");
        Assert.assertNotNull(SPANDataSource.getInstance());

    }

}

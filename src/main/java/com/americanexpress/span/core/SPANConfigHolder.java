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
/**
 * This class hold the SPANConfig object and it will be available to Framework.
 */

import com.americanexpress.span.constants.SPANConstants;
import com.americanexpress.span.exceptions.SPANException;
import com.americanexpress.span.models.DataSourceDetails;
import com.americanexpress.span.models.SPANConfig;
import com.americanexpress.span.models.SPANUserDefineKeyDetails;
import com.americanexpress.span.models.SPUserDefineKey;

import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

//I used final as it should be not override by user.
public final class SPANConfigHolder {

    private static SPANConfigHolder instance = null;

    private HoldSPANConfig holdSPANConfig;

    private SPANConfigHolder() {
    }

    public static SPANConfigHolder getInstance() {
        if (instance == null) {
            synchronized (SPANConfigHolder.class) {
                instance = new SPANConfigHolder();
            }
        }
        return instance;
    }

    // The SPANConfig gets Set only once.

    /**
     * Return final object of SPANConfig. It should be only one instance per jvm.
     *
     * @return SPANConfig
     */
    public SPANConfig getSPANConfig() {
        Objects.requireNonNull(holdSPANConfig, "holdSPANConfig should not be null");
        return holdSPANConfig.getSpanConfig();
    }


    /**
     * Get DataSource Details for a give SP User Defined key.
     *
     * @param spUserDefinedKey Stored Procedure User Defined Key
     * @return  DataSource Details
     */
    public DataSourceDetails getDataSourceDetails(final String spUserDefinedKey) {
        Set<SPANUserDefineKeyDetails> spanUserDefineKeyDetails = getSPANConfig().getSpanUserDefineKeys().values().stream().filter(spanUserDefinedValues -> {
            Set<String> filteredSpUserDefineKeys = spanUserDefinedValues.getSpUserDefineKeys().keySet()
                    .stream().filter(mapSPUserDefinedKey -> mapSPUserDefinedKey.equals(spUserDefinedKey)).collect(Collectors.toSet());
            // If there is more than one User Defined key with the same name, the method will return false.
            // SP User Defined should be unique all the time.
            return filteredSpUserDefineKeys.size() == 1;
        }).collect(Collectors.toSet());
        if (spanUserDefineKeyDetails.isEmpty()) {
            throw new SPANException("Unable to find DataSource for Stored Procedure User Defined Key: " + spUserDefinedKey);
        }
        if(spanUserDefineKeyDetails.size() != 1) {
            throw new SPANException("More than one SPAN UserDefined keys found for Stored Procedure User Defined Key: " + spUserDefinedKey);
        }
        return spanUserDefineKeyDetails.stream().map(SPANUserDefineKeyDetails ::getDataSourceDetails).collect(Collectors.toList()).get(0);
    }

    /**
     * Gets SP User Defined Key for the given SP User Defined Key name.
     *
     * @param spUserDefinedKey      SP User Defined Key name
     * @return                      SP User Defined key
     */
    public SPUserDefineKey getSPUserDefinedKey(final String spUserDefinedKey) {
        Set<Optional<SPUserDefineKey>> optionalSPUserDefineKeys = getSPANConfig().getSpanUserDefineKeys().values().stream().map(spanUserDefinedValues -> {
            Set<String> filteredSpUserDefineKeys = spanUserDefinedValues.getSpUserDefineKeys().keySet()
                    .stream().filter(mapSPUserDefinedKey -> mapSPUserDefinedKey.equals(spUserDefinedKey)).collect(Collectors.toSet());
            if (filteredSpUserDefineKeys.size() == 1) {
                return Optional.of(spanUserDefinedValues.getSpUserDefineKeys().get(filteredSpUserDefineKeys.iterator().next()));
            } else {
                SPUserDefineKey spUserDefineKeyNull = null;
                return Optional.ofNullable(spUserDefineKeyNull);
            }
        }).collect(Collectors.toSet());


        // Removing optionals.
        Set<SPUserDefineKey> spUserDefineKey = optionalSPUserDefineKeys.stream().filter(Optional::isPresent)
                .map(Optional::get).collect(Collectors.toSet());


        if (spUserDefineKey.isEmpty()) {
            throw new SPANException("Unable to find SP User Defined Key: " + spUserDefinedKey);
        }
        if(spUserDefineKey.size() != 1) {
            throw new SPANException("More than one SP UserDefined keys found for Stored Procedure User Defined Key: " + spUserDefinedKey);
        }
        return spUserDefineKey.iterator().next();
    }

    public final String getProperty(String propertyName) {
        if (Objects.isNull(holdSPANConfig)) {
            return SPANConstants.EMPTY_STRING;
        }
        return holdSPANConfig.getProperty(propertyName);
    }

    /**
     * Setting SPANConfig object, it should be set once.
     *
     * @param spanConfig
     */
    public void setSPANConfig(SPANConfig spanConfig, Properties properties) {
        holdSPANConfig = HoldSPANConfig.getInstance(spanConfig, properties);
    }

    /**
     * HoldSPANConfig - Hold SPANConfig object and it should not be override by user and it should be set by once only.
     * I have created a inner class because if i used setter method on SPANConfigHolder then i cannot instantiate final object and it is not
     * good idea to pass SPANConfig in getInstance method in order to hold SPANConfig object. User should not worry about underneath implementation.
     */
    private static final class HoldSPANConfig {

        private static HoldSPANConfig holdSPANConfig;

        private final SPANConfig spanConfig;

        private final Properties properties;

        private HoldSPANConfig(SPANConfig spanConfig, Properties properties) {
            Objects.requireNonNull(spanConfig, "spanConfig should not be null.");
            this.spanConfig = spanConfig;
            this.properties = (Properties) properties.clone();
        }


        public static
        final HoldSPANConfig getInstance(SPANConfig spanConfig, Properties properties) {

            if (holdSPANConfig == null) {
                synchronized (HoldSPANConfig.class) {
                    holdSPANConfig = new HoldSPANConfig(spanConfig, properties);
                }
            }
            return holdSPANConfig;
        }

        public final SPANConfig getSpanConfig() {
            return this.spanConfig;
        }

        public final String getProperty(String propertyName) {
            return this.properties.getProperty(propertyName);
        }
    }
}

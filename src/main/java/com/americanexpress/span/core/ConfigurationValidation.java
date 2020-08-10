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

import com.americanexpress.span.constants.SPANErrorConstants;
import com.americanexpress.span.exceptions.ConfigurationSPANException;
import com.americanexpress.span.models.SPANConfig;
import com.americanexpress.span.models.SPANUserDefineKeyDetails;
import com.americanexpress.span.models.SPUserDefineKey;

import java.util.HashSet;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;

//Validation on SPANConfig object
public class ConfigurationValidation {

    private static final String URL_OR_OTHER_DETAILS_EXCEP = "Please specify either URL or hostname, port or database. ";
    public static final String USER_DEFINED_KEY_STRING = "User Defined Key: ";

    Set<String> uniqueSPKey = new HashSet<>();

    public void validation(SPANConfig spanConfig) {

        for (String spanUserDefineKey : spanConfig.getSpanUserDefineKeys().keySet()) {

            SPANUserDefineKeyDetails spanUserDefineKeyDetails = spanConfig.getSpanUserDefineKeys()
                    .get(spanUserDefineKey);

            validateDataSourceDetails(spanUserDefineKey, spanUserDefineKeyDetails);

            if (spanUserDefineKeyDetails.getSpUserDefineKeys() == null) {
                throw new ConfigurationSPANException(SPANErrorConstants.SP_DETAILS_ERROR_MSG + spanUserDefineKey);
            }

            for (String spUserDefineKey : spanUserDefineKeyDetails.getSpUserDefineKeys().keySet()) {

                SPUserDefineKey spUserDefineKeyDetails = spanUserDefineKeyDetails.getSpUserDefineKeys()
                        .get(spUserDefineKey);

                if (uniqueSPKey.add(spUserDefineKey) == Boolean.FALSE) {
                    //exception when there are duplicate SP keys in different SPAN keys
                    throw new ConfigurationSPANException(SPANErrorConstants.SP_USER_KEY_DUPLCATE_ERROR_MSG + spUserDefineKey);
                }

                if (isBlank(spUserDefineKeyDetails.getSchema())) {

                    throw new ConfigurationSPANException(SPANErrorConstants.SCHEMA_ERROR_MSG + spUserDefineKey);
                }
                if (isBlank(spUserDefineKeyDetails.getProcedure())) {

                    throw new ConfigurationSPANException(SPANErrorConstants.PROCEDURE_ERROR_MSG + spUserDefineKey);
                }
            }

        }

    }

    /**
     * Method validates datasource details for the given SPAN User Defined Key. Throws {@link ConfigurationSPANException} in case of validation failure.
     *
     * @param spanUserDefineKey        SPAN User Defined key
     * @param spanUserDefineKeyDetails SPAN User Defined Key details
     */
    private void validateDataSourceDetails(String spanUserDefineKey, SPANUserDefineKeyDetails spanUserDefineKeyDetails) {
        if (spanUserDefineKeyDetails.getDataSourceDetails() == null) {
            throw new ConfigurationSPANException(SPANErrorConstants.DATASOURCE_DETAILS_ERROR_MSG + spanUserDefineKey);
        }

        if (isBlank(spanUserDefineKeyDetails.getDataSourceDetails().getUrl())) {
            // URL is blank, all other values hostname, port, and database should be present.
            if (isBlank(spanUserDefineKeyDetails.getDataSourceDetails().getHostName())) {
                throw new ConfigurationSPANException(SPANErrorConstants.HOSTNAME_ERROR_MSG + spanUserDefineKey);
            }

            if (isBlank(spanUserDefineKeyDetails.getDataSourceDetails().getPort())) {
                throw new ConfigurationSPANException(SPANErrorConstants.PORT_ERROR_MSG + spanUserDefineKey);
            }

            if (isBlank(spanUserDefineKeyDetails.getDataSourceDetails().getDatabase())) {
                throw new ConfigurationSPANException(SPANErrorConstants.DATABASE_ERROR_MSG + spanUserDefineKey);
            }

        } else {
            // URL is not blank, all other values hostname, port, and database should be blank.
            if (!isBlank(spanUserDefineKeyDetails.getDataSourceDetails().getHostName())) {
                throw new ConfigurationSPANException(URL_OR_OTHER_DETAILS_EXCEP + USER_DEFINED_KEY_STRING + spanUserDefineKey);
            }

            if (!isBlank(spanUserDefineKeyDetails.getDataSourceDetails().getPort())) {
                throw new ConfigurationSPANException(URL_OR_OTHER_DETAILS_EXCEP + USER_DEFINED_KEY_STRING + spanUserDefineKey);
            }

            if (!isBlank(spanUserDefineKeyDetails.getDataSourceDetails().getDatabase())) {
                throw new ConfigurationSPANException(URL_OR_OTHER_DETAILS_EXCEP + USER_DEFINED_KEY_STRING + spanUserDefineKey);
            }
        }

        if (isBlank(spanUserDefineKeyDetails.getDataSourceDetails().getUser())) {
            throw new ConfigurationSPANException(SPANErrorConstants.USER_ERROR_MSG + spanUserDefineKey);
        }

        if (isBlank(spanUserDefineKeyDetails.getDataSourceDetails().getPassword())) {
            throw new ConfigurationSPANException(SPANErrorConstants.PASSWORD_ERROR_MSG + spanUserDefineKey);
        }

    }
}

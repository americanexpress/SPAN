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
package com.americanexpress.span.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.Map;

/**
 * It will return SP_User-Define-key_1 from SPANConfigLayout.yaml file.
 *
 */

public class SPANUserDefineKeyDetails {

    @JsonProperty("datasource_details")
    private DataSourceDetails dataSourceDetails;

    @JsonProperty("sp_details")
    private Map<String, SPUserDefineKey> spUserDefineKeys;

    public DataSourceDetails getDataSourceDetails() {
        return dataSourceDetails;
    }

    public Map<String, SPUserDefineKey> getSpUserDefineKeys() {
        return spUserDefineKeys == null ? spUserDefineKeys : Collections.unmodifiableMap(spUserDefineKeys);
    }

    public void setDataSourceDetails(DataSourceDetails dataSourceDetails) {
        this.dataSourceDetails = dataSourceDetails;
    }

    public void setSpUserDefineKeys(Map<String, SPUserDefineKey> spUserDefineKeys) {
        this.spUserDefineKeys = spUserDefineKeys;
    }

    @Override
    public String toString() {
        return "SPANUserDefineKeyDetails [dataSourceDetails=" + dataSourceDetails + ", spUserDefineKeys="
                + spUserDefineKeys + "]";
    }

}

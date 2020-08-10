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

import com.americanexpress.span.constants.SPANConstants;
import com.americanexpress.span.exceptions.ConfigurationSPANException;
import com.americanexpress.span.models.SPANConfig;
import com.americanexpress.span.utility.SPANUtility;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;

public final class SPANInitialization {

    private SPANInitialization(){}

    private static final String APP_PROFILE = "APP_PROFILE";
    private static final String DEFAULT_FILE_NAME = "SPANConfig";
    private static final String YAML = ".yaml";

    /**
     * Loads the given yaml file into SPANConfig object. If no fileName given, loads the default file.
     *
     * @param fileName
     * @throws ConfigurationSPANException
     */
    public static final void initialize(String fileName) {

        if (StringUtils.isEmpty(fileName)) {
            initialize();
        } else {
            //Load the configuration file.
            loadSPANConfig(fileName);
        }

    }

    /**
     * Loads the file SPANConfig-${APP_PROFILE}.yaml. If APP_PROFILE is not found, loads the default SPANConfig.yaml file.
     *
     * @throws ConfigurationSPANException
     */
    public static final void initialize() {

        String fileName = null;
        String envProperty = SPANUtility.getEnvProperty(APP_PROFILE).orElse(SPANConstants.EMPTY_STRING);

        if (StringUtils.isNotEmpty(envProperty)) {
            fileName = DEFAULT_FILE_NAME + "-" + envProperty + YAML;
        } else {
            fileName = DEFAULT_FILE_NAME + YAML;
        }
        //Load the configuration file.
        loadSPANConfig(fileName);

    }


    /**
     * Load the YAML file and set the SPANConfig object
     *
     * @param fileName
     * @throws ConfigurationSPANException
     */
    private static void loadSPANConfig(String fileName) {
        SPANConfig spanConfig = null;
        ConfigurationValidation configurationValidation = new ConfigurationValidation();
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);
        try (InputStream inputStream = SPANInitialization.class.getClassLoader().getResourceAsStream(fileName)) {
            if (Objects.isNull(inputStream)) {
                spanConfig = mapper.readValue(Paths.get(fileName).toFile(), SPANConfig.class);
            } else {
                spanConfig = mapper.readValue(inputStream, SPANConfig.class);
            }

            Objects.requireNonNull(spanConfig, "Problem in loading file.");
            //Validate the SPAN config object. Throws an exception in case of failure.
            configurationValidation.validation(spanConfig);

            //if properties file is provided then load the properties and set into System properties.
            Properties prop = new Properties();
            if (StringUtils.isNotEmpty(spanConfig.getProperties())) {
                try (InputStream in = new FileInputStream(spanConfig.getProperties())) {
                    // load a properties file
                    prop.load(in);
                }
            }

            //Loads the SPANConfig in the singleton class.
            SPANConfigHolder.getInstance().setSPANConfig(spanConfig, prop);

        } catch (IOException mappingException) {
            throw new ConfigurationSPANException("SPAN-Exception occurred while loading the configuration file " + mappingException.getMessage(), mappingException);
        }
    }

}

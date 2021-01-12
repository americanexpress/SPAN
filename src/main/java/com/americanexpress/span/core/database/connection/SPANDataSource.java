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
import com.americanexpress.span.models.DataSourceDetails;
import com.americanexpress.span.models.SPANConfig;
import com.americanexpress.span.models.SPANUserDefineKeyDetails;
import com.americanexpress.span.utility.ExpressionEvaluation;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.isBlank;


/**
 * This class will create a map of pooled datasource object from {@link SPANConfigHolder}
 *
 *
 */
public class SPANDataSource {

    //default connection settings
    private static final String DEFAULT_CONN = "jdbc:mysql://";
    private static final String COLON = ":";
    private static final String SLASH = "/";
    private static final String DEFAULT_DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";

    private static final Logger LOGGER = LoggerFactory.getLogger(SPANDataSource.class);

    private static SPANDataSource objectInstance;
    private static Map<String, DataSource> dataSourceMap = Collections.synchronizedMap(new HashMap<>());

    private SPANDataSource(final SPANConfig spanConfig) {
        dataSourceMap.putAll(initialize(spanConfig));
    }


    public static SPANDataSource getInstance() {
        if (objectInstance == null) {
            objectInstance = new SPANDataSource(SPANConfigHolder.getInstance().getSPANConfig());
        }
        return objectInstance;
    }

    public DataSource getDataSource(final String spUserDefinedKey) {
        return dataSourceMap.get(spUserDefinedKey);
    }


    /**
     * this initialize method will create data sources for the users defined in the
     * SPANConfig and it will iterate through the each user and creates data source
     * for each user and finally returns data source map(Map< String,DataSource >)
     *
     * @param spanConfig
     * @return dataSourceMap
     */
    private Map<String, DataSource> initialize(SPANConfig spanConfig) {
        //driverClassLoaded - this will make sure that, driver class loaded once only.
        List<String> driverClassLoaded = new ArrayList<>();
        Map<String, SPANUserDefineKeyDetails> spanUserDefineKeys = spanConfig.getSpanUserDefineKeys();
        
        spanUserDefineKeys.entrySet().stream().forEach(spanUserDefineKeysEntry -> {

            DataSourceDetails dataSourceDetails = spanUserDefineKeysEntry.getValue().getDataSourceDetails();
            String driverClassName = DEFAULT_DRIVER_CLASS_NAME;
            try {
                if (driverClassLoaded.add(dataSourceDetails.getDriverClassName())) {
                    if (StringUtils.isNotEmpty(dataSourceDetails.getDriverClassName())) {
                        driverClassName = dataSourceDetails.getDriverClassName();
                    }
                    Class.forName(driverClassName);
                }

            } catch (ClassNotFoundException e) {
                LOGGER.error("Could not find the driver class name -> " + driverClassName, e);
                throw new IllegalArgumentException("Could not find the driver class name -> " + driverClassName, e);
            }

            SPANUserDefineKeyDetails spanUserDefineKeyDetails = spanUserDefineKeysEntry.getValue();


            String username = ExpressionEvaluation.getInstance().evaluate(dataSourceDetails.getUser());

            String password = ExpressionEvaluation.getInstance().evaluate(dataSourceDetails.getPassword());

            String connectURI = spanUserDefineKeyDetails.getDataSourceDetails().getUrl();

            if (isBlank(connectURI)) {
                String subProtocol = getSubProtocol(driverClassName);
                connectURI = subProtocol + spanUserDefineKeyDetails.getDataSourceDetails().getHostName() +
                        COLON + spanUserDefineKeyDetails.getDataSourceDetails().getPort() +
                        SLASH + spanUserDefineKeyDetails.getDataSourceDetails().getDatabase();
            }

            DataSource dataSource = setupDataSource(connectURI, username, password, dataSourceDetails);
            spanUserDefineKeysEntry.getValue().getSpUserDefineKeys().keySet().forEach(s -> dataSourceMap.put(s, dataSource));

        });

        return dataSourceMap;
    }


    /**
     * This method will setup the Data source object
     *
     * @param connectURI
     * @param username
     * @param password
     * @param dataSourceDetails
     * @return dataSource
     */
    private static DataSource setupDataSource(String connectURI, String username, String password, DataSourceDetails dataSourceDetails) {

        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setUrl(connectURI);
        basicDataSource.setUsername(username);
        basicDataSource.setPassword(password);
        basicDataSource.setMaxIdle(dataSourceDetails.getMaxIdle());
        basicDataSource.setMinIdle(dataSourceDetails.getMinIdle());
        basicDataSource.setMaxTotal(dataSourceDetails.getMaxActive());
        basicDataSource.setInitialSize(dataSourceDetails.getInitialSize());
        basicDataSource.setValidationQuery(dataSourceDetails.getValidationQuery());
        basicDataSource.setValidationQueryTimeout(dataSourceDetails.getValidationQueryTimeout());
        basicDataSource.setTimeBetweenEvictionRunsMillis(dataSourceDetails.getTimeBetweenEvictionRunsMillis());
        basicDataSource.setMaxWaitMillis(dataSourceDetails.getMaxWaitForConnection());
        basicDataSource.setRemoveAbandonedOnBorrow(true);
        basicDataSource.setRemoveAbandonedOnMaintenance(true);
        basicDataSource.addConnectionProperty("checkConnectionWhileIdle", String.valueOf(true));

        return basicDataSource;
    }

    /**
     * This method returns sub protocol of a connection URI based on driver class name.
     *
     * @param driverClassName
     * @return
     */
    private String getSubProtocol (String driverClassName){
        String protocol ="";
        // user can create a new pr if desired driverClassName is not listed below.
        switch (driverClassName) {
            case "com.ibm.db2.jcc.DB2Driver":
                protocol = "jdbc:db2://";
                break;
            default:
                protocol = DEFAULT_CONN;
        }
        return protocol;
    }

}



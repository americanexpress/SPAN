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

/**
 * Returns a DataSourceDetails.
 */
public class DataSourceDetails {

    @JsonProperty("hostname")
    private String hostName;

    @JsonProperty("port")
    private String port;

    @JsonProperty("database")
    private String database;

    @JsonProperty("user")
    private String user;

    @JsonProperty("password")
    private String password;

    @JsonProperty("url")
    private String url;

    @JsonProperty("maxIdle")
    private int maxIdle = 8;

    @JsonProperty("maxActive")
    private int maxActive = 1028;

    @JsonProperty("minIdle")
    private int minIdle = 8;

    @JsonProperty("initialSize")
    private int initialSize = 10;

    @JsonProperty("maxWaitForConnection")
    private int maxWaitForConnection = 2000;// # in milliseconds

    @JsonProperty("validationQuery")
    private String validationQuery; //no default

    @JsonProperty("validationQueryTimeout")
    private int validationQueryTimeout = 5;// #in seconds

    @JsonProperty("timeBetweenEvictionRunsMillis")
    private int timeBetweenEvictionRunsMillis = 10000; //# in milliseconds

    @JsonProperty("driverClassName")
    private String driverClassName;

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }


    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public int getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(int initialSize) {
        this.initialSize = initialSize;
    }

    public int getMaxWaitForConnection() {
        return maxWaitForConnection;
    }

    public void setMaxWaitForConnection(int maxWaitForConnection) {
        this.maxWaitForConnection = maxWaitForConnection;
    }

    public String getValidationQuery() {
        return validationQuery;
    }

    public void setValidationQuery(String validationQuery) {
        this.validationQuery = validationQuery;
    }

    public int getValidationQueryTimeout() {
        return validationQueryTimeout;
    }

    public void setValidationQueryTimeout(int validationQueryTimeout) {
        this.validationQueryTimeout = validationQueryTimeout;
    }

    public int getTimeBetweenEvictionRunsMillis() {
        return timeBetweenEvictionRunsMillis;
    }

    public void setTimeBetweenEvictionRunsMillis(int timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DataSourceDetails{");
        sb.append("hostName='").append(hostName).append('\'');
        sb.append(", port='").append(port).append('\'');
        sb.append(", database='").append(database).append('\'');
        sb.append(", user='").append(user).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", url='").append(url).append('\'');
        sb.append(", maxIdle=").append(maxIdle);
        sb.append(", maxActive=").append(maxActive);
        sb.append(", minIdle=").append(minIdle);
        sb.append(", initialSize=").append(initialSize);
        sb.append(", maxWaitForConnection=").append(maxWaitForConnection);
        sb.append(", validationQuery='").append(validationQuery).append('\'');
        sb.append(", validationQueryTimeout=").append(validationQueryTimeout);
        sb.append(", timeBetweenEvictionRunsMillis=").append(timeBetweenEvictionRunsMillis);
        sb.append(", driverClassName='").append(driverClassName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

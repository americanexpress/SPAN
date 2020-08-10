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
package com.americanexpress.span.constants;

public class SPANErrorConstants {
    public static final String HOSTNAME_ERROR_MSG = "'hostname' value cannot be null or empty for ";
    public static final String PORT_ERROR_MSG = "'port' value cannot be null or empty for ";
    public static final String DATABASE_ERROR_MSG = "'database' value cannot be null or empty for ";
    public static final String USER_ERROR_MSG = "'user' value cannot be null or empty for ";
    public static final String PASSWORD_ERROR_MSG = "'password' value cannot be null or empty for ";
    public static final String SCHEMA_ERROR_MSG = "'schema' value cannot be null or empty for ";
    public static final String PROCEDURE_ERROR_MSG = "'procedure' value cannot be null or empty for ";
    public static final String DATASOURCE_DETAILS_ERROR_MSG = "'datasource_details' value cannot be null or empty for ";
    public static final String SP_DETAILS_ERROR_MSG = "'sp_details' value cannot be null or empty for ";
    public static final String SP_USER_KEY_DUPLCATE_ERROR_MSG = "'sp_details' value cannot be duplicate. ";

    private SPANErrorConstants() {
    }

}

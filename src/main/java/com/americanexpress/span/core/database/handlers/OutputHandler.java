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
package com.americanexpress.span.core.database.handlers;

import java.sql.CallableStatement;

/**
 * OutputHandler - This interface has a method to transform Callable statement (after SP execution) to given Output parameter object.
 */
public interface OutputHandler<T> {

    void registerOutputParameters(CallableStatement callableStatement, Class<T> classT);

    /**
     * This method processes SP output parameters from Callable Statement object and create instance of object T (output parameters)
     *
     * @param callableStatement Callable Statement after SP execution
     * @param clazzT            Class Object for the type representing OutputParameters
     * @return                  Object for OutputParameters
     */
    T processSPOutputParameters(CallableStatement callableStatement, Class<T> clazzT);

}

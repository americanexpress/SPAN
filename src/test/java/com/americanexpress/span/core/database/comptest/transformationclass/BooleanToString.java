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
package com.americanexpress.span.core.database.comptest.transformationclass;

import com.americanexpress.span.core.database.handlers.FieldTransformation;

public class BooleanToString implements FieldTransformation<String> {

    /**
     * Converts Boolean to String. True to "Yes". False to "No".
     * Throws exception if it is used with any other source data type.
     *
     * @param fieldValue
     * @param targetClass
     * @return
     */
    @Override
    public String transform(Object fieldValue, Class<String> targetClass) {
        if (fieldValue.getClass() == Boolean.class) {
            Boolean boolVal = (Boolean) fieldValue;
            if (boolVal) {
                return "Yes";
            } else {
                return "No";
            }
        }
        throw new UnsupportedOperationException("Transformation Class BooleanToString can only be used to transform from Boolean to String.");

    }
}

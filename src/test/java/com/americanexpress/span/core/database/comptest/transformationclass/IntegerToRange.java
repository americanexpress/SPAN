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

public class IntegerToRange implements FieldTransformation<RangeEnum> {

    @Override
    public RangeEnum transform(Object fieldValue, Class<RangeEnum> targetClass) {
        if (fieldValue.getClass() == Integer.class) {
            Integer intVal = (Integer) fieldValue;
            if (intVal > 25) {
                return RangeEnum.VERY_HIGH;
            } else if (intVal >= 15) {
                return RangeEnum.HIGH;
            } else if (intVal >= 5) {
                return RangeEnum.NORMAL;
            } else {
                return RangeEnum.LOW;
            }

        }
        throw new UnsupportedOperationException("Transformation Class IntegerToRange can only be used to transform from Integer to enum - RangeEnum.");    }
}

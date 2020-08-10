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
package com.americanexpress.span.utility;

import com.americanexpress.span.exceptions.SPANException;
import org.apache.commons.lang3.StringUtils;

/**
 * This class evaluate expression, value will be extracted from SPANConfig.yaml
 * The Expression would be ${},ENC${} or without expression
 *
 *
 */
public class ExpressionEvaluation {
    private static ExpressionEvaluation ourInstance = new ExpressionEvaluation();

    public static ExpressionEvaluation getInstance() {
        return ourInstance;
    }

    private ExpressionEvaluation() {
    }

    /**
     * Evaluate the expression. For Example,
     * Key = ${test} -> extract 'test' -> then get the value from environment.
     * Key = ENC${test} -> extract 'test' -> then get the value from environment -> decrypt it.
     * Key = ENC(test) -> extract 'test' -> then decrypt
     * key = test -> return test
     *
     * @param configKey
     * @return
     */
    public String evaluate(String configKey) {

        if (StringUtils.isNotEmpty(configKey)) {
            if (configKey.startsWith(SPANUtility.EXPRESSION_START_STRING)) {
                return SPANUtility.getEnvProperty(
                        SPANUtility.getExpressionValue(configKey).orElseThrow(()
                                -> new SPANException("Expression does not evaluate for the key - " + configKey))
                ).orElseThrow(() ->
                        new SPANException("Environment variable does not found for the key - " + configKey));
            } else if (configKey.startsWith(SPANUtility.ENCRYPTION_EXPRESSION_START_STRING)) {
                return EncryptionUtility.decrypt(
                        SPANUtility.getEnvProperty(
                                SPANUtility.getEncryptionFromExpression(configKey).orElseThrow(()
                                        -> new SPANException("Encryption expression does not evaluate for the key - " + configKey))
                        ).orElseThrow(()
                                -> new SPANException("Environment variable does not found for the key - " + configKey))
                );
            } else if (configKey.startsWith(SPANUtility.ENCRYPTION_START_STRING)) {
                return EncryptionUtility.decrypt(SPANUtility.getEncryptionValueFromPlainString(configKey).orElseThrow(()
                        -> new SPANException("Plain encryption does not evaluate for the key - " + configKey)
                ));
            }
        }


        return configKey;
    }


}

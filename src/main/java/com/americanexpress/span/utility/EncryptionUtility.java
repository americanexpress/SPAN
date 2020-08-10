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


/**
 * Encrypt and Decrypt String for SPAN
 * Has Encrypt and Decrypt methods
 * These methods will accept only string as parameter and return string
 *
 */

import org.jasypt.util.text.AES256TextEncryptor;

public class EncryptionUtility {

    private EncryptionUtility(){}

    private static final String SPAN_ENC_KEY = "span@1234";
    private static AES256TextEncryptor textEncryptor = new AES256TextEncryptor();

    static{
        textEncryptor.setPassword(SPAN_ENC_KEY);
    }

    /**
     * Encrypt the raw text string.
     *
     * @param plainText
     * @return String
     */
    public static String encrypt(String plainText){
        return textEncryptor.encrypt(plainText);

    }

    /**
     * Decrypt the encrypted text string.
     * @param encryptedText
     * @return String
     */
    protected static String decrypt(String encryptedText){
        return textEncryptor.decrypt(encryptedText);

    }

}

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

import java.util.HashMap;
import java.util.Map;

/**
 * This class holding key value pair for the current Thread.
 */
public class ThreadContext {

    private ThreadContext () {}

    private static ThreadLocal<Map<String, String>> contextThreadLocal = ThreadLocal.withInitial(HashMap::new);


    public static void set(String key, String value) {
        contextThreadLocal.get().put(key, value);
    }

    public static String get(String key) {
        return contextThreadLocal.get().get(key);
    }

    public static void removeKey(String key) {
        contextThreadLocal.get().remove(key);
    }

    public static void remove() {
        contextThreadLocal.remove();
    }

}

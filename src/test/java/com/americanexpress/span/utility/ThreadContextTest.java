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

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ThreadContextTest {

    private static String key = "key";
    private static String value = "value";

    @Test
    public void set() {
        ThreadContext.set(key, value);
        assertEquals(ThreadContext.get(key),value);
    }

    @Test
    public void get() {
        assertNull(ThreadContext.get("notpresentkey"));
    }

    @Test
    public void removeKey() {
        ThreadContext.set(key, value);
        assertEquals(ThreadContext.get(key),value);
        ThreadContext.removeKey(key);
        assertNull(ThreadContext.get(key));

    }

    @Test
    public void remove() {
        ThreadContext.remove();
        assertNull(ThreadContext.get(key));
    }


}

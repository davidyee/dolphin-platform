/*
 * Copyright 2015-2016 Canoo Engineering AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.canoo.dolphin.integration;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

public class ConnectionTest extends AbstractIntegrationTest {

    @Test(dataProvider = "endpoints", description = "Tests if the client API can create a conection to the server")
    public void testConnection(String containerType, String endpoint) {
        try {
            createClientContext(endpoint);
        } catch (Exception e) {
            Assert.fail("Can not create connection for " + containerType, e);
        }
    }

    //Test cause deadlock based on eventbus deadlock (release before poll)
    @Test(dataProvider = "endpoints", enabled = false)
    public void testCreateController(String containerType, String endpoint) {
        try {
            createClientContext(endpoint).createController("ToDoController").get(10_000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            Assert.fail("Can not create controller for " + containerType, e);
        }
    }
}

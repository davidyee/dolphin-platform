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

import com.canoo.dolphin.client.ClientConfiguration;
import com.canoo.dolphin.client.ClientContext;
import com.canoo.dolphin.client.ClientContextFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class ConnectionTest {

    @Test
    public void testConnection() {
        try {
            ClientConfiguration configuration = null;

            configuration = new ClientConfiguration(new URL("http://localhost:8080/todo-app/dolphin"), r -> r.run());
            configuration.setDolphinLogLevel(Level.FINE);
            configuration.setConnectionTimeout(10_000L);
            try {
                ClientContext context = ClientContextFactory.connect(configuration).get(configuration.getConnectionTimeout(), TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                Assert.fail("Can not create connection", e);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

}

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
package com.canoo.dolphin.test.scopes;

import com.canoo.dolphin.mapping.DolphinBean;
import com.canoo.dolphin.mapping.Property;

@DolphinBean
public class ScopeModel {

    private Property<String> requestServiceId;

    private Property<String> clientServiceId;

    private Property<String> sessionServiceId;

    private Property<String> singletonServiceId;

    public Property<String> requestServiceIdProperty() {
        return requestServiceId;
    }

    public Property<String> clientServiceIdProperty() {
        return clientServiceId;
    }

    public Property<String>sessionServiceIdProperty() {
        return sessionServiceId;
    }

    public Property<String> singletonServiceIdProperty() {
        return singletonServiceId;
    }
}

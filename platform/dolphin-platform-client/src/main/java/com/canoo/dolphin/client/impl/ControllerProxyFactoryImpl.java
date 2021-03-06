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
package com.canoo.dolphin.client.impl;

import com.canoo.dolphin.client.ControllerProxy;
import com.canoo.dolphin.impl.InternalAttributesBean;
import com.canoo.dolphin.impl.PlatformConstants;
import com.canoo.dolphin.util.Assert;
import org.opendolphin.core.client.ClientDolphin;

import java.util.concurrent.CompletableFuture;

public class ControllerProxyFactoryImpl implements ControllerProxyFactory {

    private final ClientPlatformBeanRepository platformBeanRepository;

    private final DolphinCommandHandler dolphinCommandHandler;

    private final ClientDolphin clientDolphin;

    public ControllerProxyFactoryImpl(ClientPlatformBeanRepository platformBeanRepository, DolphinCommandHandler dolphinCommandHandler, ClientDolphin clientDolphin) {
        Assert.requireNonNull(platformBeanRepository, "platformBeanRepository");
        Assert.requireNonNull(dolphinCommandHandler, "dolphinCommandHandler");
        Assert.requireNonNull(clientDolphin, "clientDolphin");
        this.platformBeanRepository = platformBeanRepository;
        this.dolphinCommandHandler = dolphinCommandHandler;
        this.clientDolphin = clientDolphin;
    }

    @Override
    public <T> CompletableFuture<ControllerProxy<T>> create(String name) {
        Assert.requireNonBlank(name, "name");
        final InternalAttributesBean bean = platformBeanRepository.getInternalAttributesBean();
        bean.setControllerName(name);

        return dolphinCommandHandler.invokeDolphinCommand(PlatformConstants.REGISTER_CONTROLLER_COMMAND_NAME).thenApply((v) -> {
            return new ControllerProxyImpl<>(bean.getControllerId(), (T) bean.getModel(), clientDolphin, platformBeanRepository);
        });
    }
}

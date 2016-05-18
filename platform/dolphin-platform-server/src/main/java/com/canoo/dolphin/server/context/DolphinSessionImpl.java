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
package com.canoo.dolphin.server.context;

import com.canoo.dolphin.server.DolphinSession;
import com.canoo.dolphin.util.Assert;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Default implementation of {@link DolphinSession} that uses a map internally to store all attributes
 */
public class DolphinSessionImpl implements DolphinSession {

    private final Map<String, Object> store;

    private final String dolphinSessionId;

    private final DolphinSessionProvider dolphinSessionProvider;

    private final DolphinSessionRunner dolphinSessionRunner;

    public DolphinSessionImpl(String dolphinSessionId, DolphinSessionProvider dolphinSessionProvider, DolphinSessionRunner dolphinSessionRunner) {
        this.dolphinSessionId = Assert.requireNonBlank(dolphinSessionId, "dolphinSessionId");
        this.dolphinSessionProvider = Assert.requireNonNull(dolphinSessionProvider, "dolphinSessionProvider");
        this.dolphinSessionRunner = Assert.requireNonNull(dolphinSessionRunner, "dolphinSessionRunner");
        this.store = new ConcurrentHashMap<>();
    }

    @Override
    public void setAttribute(String name, Object value) {
        store.put(name, value);
    }

    @Override
    public Object getAttribute(String name) {
        return store.get(name);
    }

    @Override
    public void removeAttribute(String name) {
        store.remove(name);
    }

    @Override
    public Set<String> getAttributeNames() {
        return Collections.unmodifiableSet(store.keySet());
    }

    @Override
    public void invalidate() {
        store.clear();
    }

    @Override
    public String getId() {
        return dolphinSessionId;
    }

    @Override
    public Future<Void> runLater(final Runnable runnable) {
        Assert.requireNonNull(runnable, "runnable");
        return runLater(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                runnable.run();
                return null;
            }
        });
    }

    @Override
    public <V> Future<V> runLater(final Callable<V> callable) {
        Assert.requireNonNull(callable, "callable");
        if(isCurrentSession()) {
            throw new DolphinCommandException("runLater / runAndWait can't be called from the same Dolphin Platform context since this can end in a deadlock!");
        }
        return dolphinSessionRunner.runLater(callable);
    }

    @Override
    public void runAndWait(final Runnable runnable)  throws InvocationTargetException, InterruptedException {
        Assert.requireNonNull(runnable, "runnable");
        runAndWait(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                runnable.run();
                return null;
            }
        });
    }

    @Override
    public <V> V runAndWait(final Callable<V> callable)  throws InvocationTargetException, InterruptedException {
        Assert.requireNonNull(callable, "callable");
        try {
            return runLater(callable).get();
        } catch (InterruptedException e) {
            throw e;
        } catch (ExecutionException e) {
            throw new InvocationTargetException(e);
        }
    }

    public boolean isCurrentSession() {
        DolphinSession currentSession = dolphinSessionProvider.getCurrentDolphinSession();
        if(currentSession != null && currentSession.equals(this)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DolphinSessionImpl that = (DolphinSessionImpl) o;

        return dolphinSessionId.equals(that.dolphinSessionId);

    }

    @Override
    public int hashCode() {
        return dolphinSessionId.hashCode();
    }
}

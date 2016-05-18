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
import org.testng.Assert;
import org.testng.annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Created by hendrikebbers on 18.03.16.
 */
public class DolphinSessionImplTest {

    @Test
    public void testid() {
        //given:
        DolphinSession dolphinSession = createSessionForTest("test-id", true);

        //then:
        assertEquals("test-id", dolphinSession.getId());
    }

    @Test
    public void testidFromWrongContext() {
        //given:
        DolphinSession dolphinSession = createSessionForTest("test-id", false);

        //then:
        assertEquals("test-id", dolphinSession.getId());
    }

    @Test
    public void testAddAttribute() {
        //given:
        DolphinSession dolphinSession = createSessionForTest("test-id", true);

        //when:
        dolphinSession.setAttribute("test-attribute", "Hello Dolphin Session");

        //then:
        assertEquals(1, dolphinSession.getAttributeNames().size());
        assertTrue(dolphinSession.getAttributeNames().contains("test-attribute"));
        assertEquals("Hello Dolphin Session", dolphinSession.getAttribute("test-attribute"));
    }

    @Test(expectedExceptions = DolphinContextException.class)
    public void testSetAttributeFromWrongContext() {
        //given:
        DolphinSession dolphinSession = createSessionForTest("test-id", false);

        //when:
        dolphinSession.setAttribute("test-attribute", "Hello Dolphin Session");
    }

    @Test
    public void testNullAttribute() {
        //given:
        DolphinSession dolphinSession = createSessionForTest("test-id", true);

        //then:
        assertEquals(0, dolphinSession.getAttributeNames().size());
        Assert.assertNull(dolphinSession.getAttribute("test-attribute"));
    }

    @Test(expectedExceptions = DolphinContextException.class)
    public void testGetAttributeFromWrongContext() {
        //given:
        DolphinSession dolphinSession = createSessionForTest("test-id", false);

        //then:
        dolphinSession.getAttribute("test-attribute");
    }

    @Test(expectedExceptions = DolphinContextException.class)
    public void testGetAttributeNamesFromWrongContext() {
        //given:
        DolphinSession dolphinSession = createSessionForTest("test-id", false);

        //when:
        dolphinSession.getAttributeNames();
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testImmutableAttributeSet() {
        //given:
        DolphinSession dolphinSession = createSessionForTest("test-id", true);

        //then:
        dolphinSession.getAttributeNames().add("att");
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testImmutableAttributeSet2() {
        //given:
        DolphinSession dolphinSession = createSessionForTest("test-id", true);

        //when:
        dolphinSession.setAttribute("test-attribute", "Hello Dolphin Session");

        //then:
        dolphinSession.getAttributeNames().remove("test-attribute");
    }

    @Test
    public void testRemoveAttribute() {
        //given:
        DolphinSession dolphinSession = createSessionForTest("test-id", true);

        //when:
        dolphinSession.setAttribute("test-attribute", "Hello Dolphin Session");
        dolphinSession.removeAttribute("test-attribute");

        //then:
        assertEquals(0, dolphinSession.getAttributeNames().size());
        Assert.assertNull(dolphinSession.getAttribute("test-attribute"));
    }

    @Test(expectedExceptions = DolphinContextException.class)
    public void testRemoveAttributeNamesFromWrongContext() {
        //given:
        DolphinSession dolphinSession = createSessionForTest("test-id", false);

        //when:
        dolphinSession.removeAttribute("bla");
    }

    @Test
    public void testMultipleAttributes() {
        //given:
        DolphinSession dolphinSession = createSessionForTest("test-id", true);

        //when:
        dolphinSession.setAttribute("test-attribute1", "Hello Dolphin Session");
        dolphinSession.setAttribute("test-attribute2", "Yeah!");
        dolphinSession.setAttribute("test-attribute3", "Dolphin Platform");

        //then:
        assertEquals(3, dolphinSession.getAttributeNames().size());
        assertTrue(dolphinSession.getAttributeNames().contains("test-attribute1"));
        assertTrue(dolphinSession.getAttributeNames().contains("test-attribute2"));
        assertTrue(dolphinSession.getAttributeNames().contains("test-attribute3"));
        assertEquals("Hello Dolphin Session", dolphinSession.getAttribute("test-attribute1"));
        assertEquals("Yeah!", dolphinSession.getAttribute("test-attribute2"));
        assertEquals("Dolphin Platform", dolphinSession.getAttribute("test-attribute3"));
    }

    @Test
    public void testInvalidate() {
        //given:
        DolphinSession dolphinSession = createSessionForTest("test-id", true);

        //when:
        dolphinSession.setAttribute("test-attribute1", "Hello Dolphin Session");
        dolphinSession.setAttribute("test-attribute2", "Yeah!");
        dolphinSession.setAttribute("test-attribute3", "Dolphin Platform");
        dolphinSession.invalidate();

        //then:
        assertEquals(0, dolphinSession.getAttributeNames().size());
        Assert.assertFalse(dolphinSession.getAttributeNames().contains("test-attribute1"));
        Assert.assertFalse(dolphinSession.getAttributeNames().contains("test-attribute2"));
        Assert.assertFalse(dolphinSession.getAttributeNames().contains("test-attribute3"));
        Assert.assertNull(dolphinSession.getAttribute("test-attribute1"));
        Assert.assertNull(dolphinSession.getAttribute("test-attribute2"));
        Assert.assertNull(dolphinSession.getAttribute("test-attribute3"));
    }

    @Test(expectedExceptions = DolphinContextException.class)
    public void testInvalidateFromWrongContext() {
        //given:
        DolphinSession dolphinSession = createSessionForTest("test-id", false);

        //when:
        dolphinSession.invalidate();
    }

    @Test(expectedExceptions = DolphinContextException.class)
    public void testRunRunnableLater() {
        //given:
        DolphinSession dolphinSession = createSessionForTest("test-id", true);

        //when:
        dolphinSession.runLater(new Runnable() {
            @Override
            public void run() {
            }
        });
    }

    @Test
    public void testRunRunnableLaterFromWrongContext() {
        //given:
        DolphinSession dolphinSession = createSessionForTest("test-id", false);
        List<String> results = new ArrayList<>();

        //when:
        dolphinSession.runLater(new Runnable() {
            @Override
            public void run() {
                results.add("Hello");
            }
        });

        //then:
        assertEquals(1, results.size());
        assertTrue(results.contains("Hello"));
    }


    @Test(expectedExceptions = DolphinContextException.class)
    public void testRunCallableLater() {
        //given:
        DolphinSession dolphinSession = createSessionForTest("test-id", true);

        //when:
        dolphinSession.runLater(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return null;
            }
        });
    }

    @Test
    public void testRunCallableLaterFromWrongContext() throws ExecutionException, InterruptedException {
        //given:
        DolphinSession dolphinSession = createSessionForTest("test-id", false);

        //when:
        String content = dolphinSession.runLater(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "Hello";
            }
        }).get();

        //then:
        assertEquals("Hello", content);
    }

    @Test(expectedExceptions = DolphinContextException.class)
    public void testRunRunnableAndWait() throws InvocationTargetException, InterruptedException {
        //given:
        DolphinSession dolphinSession = createSessionForTest("test-id", true);

        //when:
        dolphinSession.runAndWait(new Runnable() {
            @Override
            public void run() {
            }
        });
    }

    @Test
    public void testRunRunnableAndWaitFromWrongContext() throws InvocationTargetException, InterruptedException {
        //given:
        DolphinSession dolphinSession = createSessionForTest("test-id", false);
        List<String> results = new ArrayList<>();

        //when:
        dolphinSession.runAndWait(new Runnable() {
            @Override
            public void run() {
                results.add("Hello");
            }
        });

        //then:
        assertEquals(1, results.size());
        assertTrue(results.contains("Hello"));
    }

    @Test(expectedExceptions = DolphinContextException.class)
    public void testRunCallableAndWait() throws InvocationTargetException, InterruptedException {
        //given:
        DolphinSession dolphinSession = createSessionForTest("test-id", true);

        //when:
        dolphinSession.runAndWait(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return null;
            }
        });
    }

    @Test
    public void testRunCallableAndWaitFromWrongContext() throws InvocationTargetException, InterruptedException {
        //given:
        DolphinSession dolphinSession = createSessionForTest("test-id", false);

        //when:
        String content = dolphinSession.runAndWait(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "Hello";
            }
        });

        //then:
        assertEquals("Hello", content);
    }

    private DolphinSession createSessionForTest(String id, boolean activeSession) {

        List<DolphinSession> sessions = new ArrayList<>();

        DolphinSessionProvider dolphinSessionProvider = new DolphinSessionProvider() {
            @Override
            public DolphinSession getCurrentDolphinSession() {
                return sessions.get(0);
            }
        };

        DolphinSessionRunner dolphinSessionRunner = new DolphinSessionRunner() {
            @Override
            public <V> Future<V> runLater(Callable<V> callable) {
                CompletableFuture<V> ret = new CompletableFuture<>();
                try {
                    ret.complete(callable.call());
                } catch (Exception e) {
                    ret.completeExceptionally(e);
                }
                return ret;
            }
        };

        if (!activeSession) {
            sessions.add(new DolphinSessionImpl(UUID.randomUUID().toString(), dolphinSessionProvider, dolphinSessionRunner));
        }

        DolphinSession testSession = new DolphinSessionImpl(id, dolphinSessionProvider, dolphinSessionRunner);
        sessions.add(testSession);
        return testSession;
    }
}

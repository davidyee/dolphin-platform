package com.canoo.dolphin.server.event.impl;

import com.canoo.dolphin.server.event.Message;
import com.canoo.dolphin.server.event.MessageListener;
import com.canoo.dolphin.server.event.Topic;
import com.canoo.dolphin.server.event.impl2.DolphinSessionEventMonitor;
import com.canoo.dolphin.server.event.impl2.MessageImpl;
import org.testng.annotations.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

/**
 * Created by hendrikebbers on 04.03.16.
 */
public class DolphinSessionEventMonitorTest {

    @Test
    public void testAsync() {
        //given:
        final DolphinSessionEventMonitor monitor = new DolphinSessionEventMonitor();
        final Topic<Void> topic = new Topic<>();
        final Check check = addCheckListener(monitor, topic);
        final Future<?> runner = createBackgroundWaitForEvents(monitor);

        //when:
        publishDummyEvent(monitor, topic);
        try {
            //Wait some time to get the event handled
            runner.get(100, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
        }

        //then:
        assertEquals(1, check.getCheckCount());
    }

    @Test
    public void testAsyncRelease() {
        //given:
        final DolphinSessionEventMonitor monitor = new DolphinSessionEventMonitor();
        final Topic<Void> topic = new Topic<>();

        //when:
        Future<?> runner = createBackgroundWaitForEvents(monitor);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        boolean released = monitor.release();
        assertEquals(released, true);
        try {
            runner.get(100, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            fail("ERROR", e);
        }
        runner = createBackgroundWaitForEvents(monitor);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        released = monitor.release();
        assertEquals(released, true);
        try {
            runner.get(100, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            fail("ERROR", e);
        }
        runner = createBackgroundWaitForEvents(monitor);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        released = monitor.release();
        assertEquals(released, true);
        try {
            runner.get(500, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            fail("ERROR", e);
        }
    }

    @Test
    public void testNoAutomaticRelease() {
        //given:
        final DolphinSessionEventMonitor monitor = new DolphinSessionEventMonitor();

        //when:
        Future<?> runner = createBackgroundWaitForEvents(monitor);

        //then:
        try {
            runner.get(3, TimeUnit.SECONDS);
            fail("ERROR");
        } catch (Exception e) {
        }
    }

    @Test
    public void testSync() {
        //given:
        final DolphinSessionEventMonitor monitor = new DolphinSessionEventMonitor();
        final Topic<Void> topic = new Topic<>();
        final Check check = addCheckListener(monitor, topic);

        //when:
        monitor.setEventPublisher(true);
        publishDummyEvent(monitor, topic);
        monitor.setEventPublisher(false);

        //then:
        assertEquals(1, check.getCheckCount());
    }

    @Test
    public void testReleaseResponse() {
        //given:
        final DolphinSessionEventMonitor monitor = new DolphinSessionEventMonitor();
        final Topic<Void> topic = new Topic<>();
        final Check check = addCheckListener(monitor, topic);

        //when:
        boolean released = monitor.release();

        //then:
        assertEquals(released, false);
    }

    @Test
    public void testAsyncNotHandleDirectly() {
        //given:
        final DolphinSessionEventMonitor monitor = new DolphinSessionEventMonitor();
        final Topic<Void> topic = new Topic<>();
        final Check check = addCheckListener(monitor, topic);

        //when:
        publishDummyEvent(monitor, topic);

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //then:
        assertEquals(0, check.getCheckCount());
    }

    private <T> void publishDummyEvent(final DolphinSessionEventMonitor monitor, Topic<T> topic) {
        monitor.publishEvent(new MessageImpl<T>(topic, null));
    }

    private Future<?> createBackgroundWaitForEvents(final DolphinSessionEventMonitor monitor) {
        return Executors.newSingleThreadExecutor().submit(new Runnable() {
            @Override
            public void run() {
                monitor.waitForEvents();
            }
        });
    }

    private <T> Check addCheckListener(final DolphinSessionEventMonitor monitor, Topic<T> topic) {
        final Check check = new Check();

        monitor.addListener(topic, new MessageListener<T>() {
            @Override
            public void onMessage(Message<T> message) {
                check.check();
            }
        });
        return check;
    }
}

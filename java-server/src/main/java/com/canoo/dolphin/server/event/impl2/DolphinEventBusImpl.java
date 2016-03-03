package com.canoo.dolphin.server.event.impl2;

import com.canoo.dolphin.event.Subscription;
import com.canoo.dolphin.server.event.DolphinEventBus;
import com.canoo.dolphin.server.event.MessageListener;
import com.canoo.dolphin.server.event.Topic;

/**
 * Created by hendrikebbers on 03.03.16.
 */
public class DolphinEventBusImpl implements DolphinEventBus {

    private DolphinEventPublisher eventPublisher;

    private DolphinSessionEventMonitor monitor;

    public DolphinEventBusImpl(DolphinEventPublisher eventPublisher, DolphinSessionEventMonitor monitor) {
        this.eventPublisher = eventPublisher;
        this.monitor = monitor;
    }

    @Override
    public <T> void publish(Topic<T> topic, T data) {
        monitor.setEventPublisher(true);
        try {
            eventPublisher.publish(topic, data);
        } finally {
            monitor.setEventPublisher(false);
        }
    }

    @Override
    public <T> Subscription subscribe(final Topic<T> topic, final MessageListener<? super T> handler) {
        monitor.addListener(topic, handler);
        return new Subscription() {
            @Override
            public void unsubscribe() {
                monitor.removeListener(topic, handler);
            }
        };
    }
}

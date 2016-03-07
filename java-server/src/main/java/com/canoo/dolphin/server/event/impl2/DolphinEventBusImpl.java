package com.canoo.dolphin.server.event.impl2;

import com.canoo.dolphin.event.Subscription;
import com.canoo.dolphin.server.event.DolphinEventBus;
import com.canoo.dolphin.server.event.MessageListener;
import com.canoo.dolphin.server.event.Topic;
import com.canoo.dolphin.util.Assert;

/**
 * Implementation of the {@link DolphinEventBus} interface that is based on {@link DolphinEventPublisher} and {@link EventMonitorSupplier}
 */
public class DolphinEventBusImpl implements DolphinEventBus {

    private final DolphinEventPublisher eventPublisher;

    private final EventMonitorSupplier monitorSupplier;

    public DolphinEventBusImpl(final DolphinEventPublisher eventPublisher, final EventMonitorSupplier monitorSupplier) {
        this.eventPublisher = Assert.requireNonNull(eventPublisher, "eventPublisher");
        this.monitorSupplier = Assert.requireNonNull(monitorSupplier, "monitorSupplier");
    }

    @Override
    public <T> void publish(final Topic<T> topic, final T data) {
        Assert.requireNonNull(topic, "topic");
        final DolphinSessionEventMonitor monitor = monitorSupplier.getCurrentMonitor();
        if(monitor != null) {
            monitor.setEventPublisher(true);
        }
        try {
            eventPublisher.publish(topic, data);
        } finally {
            if(monitor != null) {
                monitor.setEventPublisher(false);
            }
        }
    }

    @Override
    public <T> Subscription subscribe(final Topic<T> topic, final MessageListener<? super T> listener) {
        Assert.requireNonNull(topic, "topic");
        Assert.requireNonNull(listener, "listener");
        final DolphinSessionEventMonitor monitor = monitorSupplier.getCurrentMonitor();
        if(monitor == null) {
            //TODO: Custom "NotInContext" Exception
            throw new RuntimeException("Not in Dolphin Context");
        }
        monitor.addListener(topic, listener);
        return new Subscription() {
            @Override
            public void unsubscribe() {
                monitor.removeListener(topic, listener);
            }
        };
    }
}

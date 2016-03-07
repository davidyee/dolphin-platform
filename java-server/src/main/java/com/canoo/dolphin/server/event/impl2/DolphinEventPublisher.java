package com.canoo.dolphin.server.event.impl2;

import com.canoo.dolphin.server.event.Topic;
import com.canoo.dolphin.util.Assert;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This class can publshes a message (see {@link com.canoo.dolphin.server.event.Message})
 * to all registered monitors (see {@link DolphinSessionEventMonitor})
 */
public class DolphinEventPublisher {

    private final List<DolphinSessionEventMonitor> monitors;

    public DolphinEventPublisher() {
        this.monitors = new CopyOnWriteArrayList<>();
    }

    public <T> void publish(final Topic<T> topic, final T data) {
        Assert.requireNonNull(topic, "topic");
        Assert.requireNonNull(data, "data");
        for(DolphinSessionEventMonitor monitor : monitors) {
            if(monitor.hasTopic(topic)) {
                monitor.publishEvent(new MessageImpl(topic, data));
            }
        }
    }

    public void addMonitor(final DolphinSessionEventMonitor monitor) {
        Assert.requireNonNull(monitor, "monitor");
        monitors.add(monitor);
    }

    public void removeMonitor(final DolphinSessionEventMonitor monitor) {
        Assert.requireNonNull(monitor, "monitor");
        monitors.remove(monitor);
    }

}

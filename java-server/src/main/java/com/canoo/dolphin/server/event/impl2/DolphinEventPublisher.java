package com.canoo.dolphin.server.event.impl2;

import com.canoo.dolphin.server.event.Topic;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by hendrikebbers on 03.03.16.
 */
public class DolphinEventPublisher {

    private List<DolphinSessionEventMonitor> monitors;

    public DolphinEventPublisher() {
        this.monitors = new CopyOnWriteArrayList<>();
    }

    public <T> void publish(Topic<T> topic, T data) {
        for(DolphinSessionEventMonitor monitor : monitors) {
            if(monitor.hasTopic(topic)) {
                monitor.publishEvent(new MessageImpl(topic, data));
            }
        }
    }

    public void addMonitor(DolphinSessionEventMonitor monitor) {
        monitors.add(monitor);
    }

    public void removeMonitor(DolphinSessionEventMonitor monitor) {
        monitors.remove(monitor);
    }

}

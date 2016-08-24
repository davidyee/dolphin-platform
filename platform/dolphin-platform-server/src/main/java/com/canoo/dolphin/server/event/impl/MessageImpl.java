package com.canoo.dolphin.server.event.impl;

import com.canoo.dolphin.server.event.Message;
import com.canoo.dolphin.server.event.Topic;

public final class MessageImpl<T> implements Message<T> {

    private final Topic<T> topic;

    private final T data;

    private final long timestamp;

    protected MessageImpl(Topic<T> topic, T data) {
        this.topic = topic;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public Topic<T> getTopic() {
        return topic;
    }

    @Override
    public T getData() {
        return data;
    }

    @Override
    public long getSendTimestamp() {
        return timestamp;
    }
}
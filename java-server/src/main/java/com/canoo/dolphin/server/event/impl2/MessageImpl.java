package com.canoo.dolphin.server.event.impl2;

import com.canoo.dolphin.server.event.Message;
import com.canoo.dolphin.server.event.Topic;
import com.canoo.dolphin.util.Assert;

/**
 * Default implementation of {@link Message}
 */
public class MessageImpl<T> implements Message<T> {

    private final Topic<T> topic;

    private final T data;

    private final long timestamp;

    public MessageImpl(Topic<T> topic, T data) {
        Assert.requireNonNull(topic, "topic");
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

package com.canoo.dolphin.server.event.impl2;

import com.canoo.dolphin.server.event.Message;
import com.canoo.dolphin.server.event.MessageListener;
import com.canoo.dolphin.server.event.Topic;
import com.canoo.dolphin.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DolphinSessionEventMonitor {

    private final Map<Topic<?>, List<MessageListener<?>>> registeredListeners;

    private final Lock mapLock = new ReentrantLock();

    private final Queue<Message> eventQueue;

    private final Lock eventLock = new ReentrantLock();

    private final Condition eventAddedCondition = eventLock.newCondition();

    private boolean interrupted = false;

    private boolean inEventLoop = false;

    private boolean eventPublisher = false;

    public DolphinSessionEventMonitor() {
        registeredListeners = new HashMap<>();
        eventQueue = new LinkedList<>();
    }

    public void publishEvent(final Message<?> message) {
        Assert.requireNonNull(message, "message");
        if(isEventPublisher()) {
            handle(message);
        } else {
            eventLock.lock();
            try {
                eventQueue.add(message);
                eventAddedCondition.signal();
            } finally {
                eventLock.unlock();
            }
        }
    }

    public void waitForEvents() {
        eventLock.lock();
        try {
            inEventLoop = true;
            interrupted = false;
            while (!interrupted) {
                while (!interrupted && !eventQueue.isEmpty()) {
                    Message message = eventQueue.poll();
                    try {
                        handle(message);
                    } catch (Exception e) {
                        //TODO: Exception in message handling
                        e.printStackTrace();
                    }
                }
                if (interrupted) {
                    return;
                }
                try {
                    eventAddedCondition.await();
                } catch (InterruptedException e) {
                }
            }
        } finally {
            inEventLoop = false;
            eventLock.unlock();
        }
    }

    public boolean release() {
        eventLock.lock();
        try {
            if(!inEventLoop) {
                //This will happen if a release was called but no thread is waiting in the waitForEvents
                //In that case the client should send another release
                return false;
            }

            // I don't use thread.interrupt() here because this might interrupt the handling of a message
            interrupted = true;
            eventAddedCondition.signal();
            return true;
        } finally {
            eventLock.unlock();
        }
    }

    public boolean hasTopic(final Topic topic) {
        Assert.requireNonNull(topic, "topic");
        mapLock.lock();
        try {
            return registeredListeners.keySet().contains(topic);
        } finally {
            mapLock.unlock();
        }
    }

    private void handle(final Message message) {
        Assert.requireNonNull(message, "message");
        Topic<?> topic = message.getTopic();
        List<MessageListener<?>> listeners = null;

        mapLock.lock();
        try {
            List<MessageListener<?>> currentListeners = registeredListeners.get(topic);
            if (currentListeners != null) {
                listeners = new ArrayList<>(currentListeners);
            }
        } finally {
            mapLock.unlock();
        }
        if (listeners != null) {
            for (MessageListener<?> listener : listeners) {
                listener.onMessage(message);
            }
        }
    }

    public <T> void removeListener(final Topic<T> topic, final MessageListener<? super T> handler) {
        Assert.requireNonNull(topic, "topic");
        Assert.requireNonNull(handler, "handler");
        mapLock.lock();
        try {
            List<MessageListener<?>> listenersForTopic = registeredListeners.get(topic);
            if (listenersForTopic != null) {
                listenersForTopic.remove(handler);
            }
        } finally {
            mapLock.unlock();
        }
    }

    public <T> void addListener(final Topic<T> topic, final MessageListener<? super T> handler) {
        Assert.requireNonNull(topic, "topic");
        Assert.requireNonNull(handler, "handler");
        mapLock.lock();
        try {
            List<MessageListener<?>> listenersForTopic = registeredListeners.get(topic);
            if (listenersForTopic == null) {
                listenersForTopic = new ArrayList<>();
                registeredListeners.put(topic, listenersForTopic);
            }
            listenersForTopic.add(handler);
        } finally {
            mapLock.unlock();
        }
    }

    public boolean isEventPublisher() {
        return eventPublisher;
    }

    public void setEventPublisher(boolean eventPublisher) {
        if(inEventLoop) {
            throw new RuntimeException("Can not be event publisher if in event loop!");
        }
        this.eventPublisher = eventPublisher;
    }
}

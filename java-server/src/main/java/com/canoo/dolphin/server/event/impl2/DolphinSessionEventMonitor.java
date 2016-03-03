package com.canoo.dolphin.server.event.impl2;

import com.canoo.dolphin.server.event.Message;
import com.canoo.dolphin.server.event.MessageListener;
import com.canoo.dolphin.server.event.Topic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DolphinSessionEventMonitor {

    public Map<Topic<?>, List<MessageListener<?>>> registeredListeners;

    private Lock mapLock = new ReentrantLock();

    public Queue<Message> eventQueue;

    private Lock eventLock = new ReentrantLock();

    private Condition eventAddedCondition = eventLock.newCondition();

    private boolean interrupted = false;

    private boolean eventPublisher = false;

    public void publishEvent(final Message<?> message) {
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
            interrupted = false;
            while (!interrupted) {
                while (!interrupted && !eventQueue.isEmpty()) {
                    Message message = eventQueue.poll();
                    handle(message);
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
            eventLock.unlock();
        }
    }

    public void release() {
        eventLock.lock();
        try {
            interrupted = true;
            eventAddedCondition.signal();
        } finally {
            eventLock.unlock();
        }
    }

    private <T> T callLocked(Callable<T> callable) {
        try {
            eventLock.lock();
            return callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            eventLock.unlock();
        }
    }

    public boolean hasTopic(Topic topic) {
        mapLock.lock();
        try {
            return registeredListeners.keySet().contains(topic);
        } finally {
            mapLock.unlock();
        }
    }

    private void handle(Message message) {
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
        this.eventPublisher = eventPublisher;
    }
}

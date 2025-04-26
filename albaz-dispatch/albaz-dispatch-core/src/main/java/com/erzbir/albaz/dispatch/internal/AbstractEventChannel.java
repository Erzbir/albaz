package com.erzbir.albaz.dispatch.internal;


import com.erzbir.albaz.dispatch.channel.EventChannel;
import com.erzbir.albaz.dispatch.event.Event;
import com.erzbir.albaz.dispatch.listener.ListenerHandle;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * @author Erzbir
 * @see EventChannel
 * @since 1.0.0
 */
public abstract class AbstractEventChannel<E extends Event> implements EventChannel<E> {
    protected final Class<E> baseEventClass;
    protected final AtomicBoolean activated = new AtomicBoolean(true);

    public AbstractEventChannel(Class<E> baseEventClass) {
        this.baseEventClass = baseEventClass;
    }

    public abstract <T extends E> ListenerHandle subscribeAlways(Class<T> eventType, Consumer<T> handle);

    public void close() {
        activated.set(false);
    }

    public void open() {
        activated.set(true);
    }

    @Override
    public boolean isClosed() {
        return !activated.get();
    }
}

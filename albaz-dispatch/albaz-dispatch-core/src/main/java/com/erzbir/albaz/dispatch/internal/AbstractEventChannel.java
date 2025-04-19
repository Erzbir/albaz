package com.erzbir.albaz.dispatch.internal;


import com.erzbir.albaz.common.Interceptor;
import com.erzbir.albaz.dispatch.channel.EventChannel;
import com.erzbir.albaz.dispatch.event.Event;
import com.erzbir.albaz.dispatch.listener.Listener;
import com.erzbir.albaz.dispatch.listener.ListenerHandle;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * @author Erzbir
 * @see EventChannel
 * @since 1.0.0
 */
public abstract class AbstractEventChannel<E extends Event> implements EventChannel<E> {
    protected Class<E> baseEventClass;
    protected List<Interceptor<Listener<E>>> interceptors = new ArrayList<>();
    protected AtomicBoolean activated = new AtomicBoolean(true);

    public AbstractEventChannel(Class<E> baseEventClass) {
        this.baseEventClass = baseEventClass;
    }

    public Class<E> getBaseEventClass() {
        return baseEventClass;
    }


    public abstract <T extends E> ListenerHandle subscribeAlways(Class<T> eventType, Consumer<T> handle);

//    public abstract <T extends E> void forward(EventChannel<E> channel);
//    protected abstract void callListeners(E event);

    public void addInterceptor(Interceptor<Listener<E>> listenerInterceptor) {
        interceptors.add(listenerInterceptor);
    }

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

package com.erzbir.albaz.dispatch.internal;


import com.erzbir.albaz.common.Context;
import com.erzbir.albaz.dispatch.ContextDelegate;
import com.erzbir.albaz.dispatch.EventContext;
import com.erzbir.albaz.dispatch.Listener;
import com.erzbir.albaz.dispatch.ListenerContext;

import java.util.Map;
import java.util.stream.Stream;

/**
 * @author Erzbir
 * @since 1.0.0
 */
@SuppressWarnings("rawtypes")
class DefaultListenerContext implements ListenerContext {
    private final EventContext eventContext;
    private final Listener listener;
    private final ContextDelegate contextDelegate = new ContextDelegate();

    public DefaultListenerContext(EventContext eventContext, Listener listener) {
        this.eventContext = eventContext;
        this.listener = listener;
    }

    @Override
    public EventContext getEventContext() {
        return eventContext;
    }

    @Override
    public Listener getListener() {
        return listener;
    }

    @Override
    public Context put(Object key, Object value) {
        return contextDelegate.put(key, value);
    }

    @Override
    public Context delete(Object key) {
        return contextDelegate.delete(key);
    }

    @Override
    public <T> T get(Object key) {
        return contextDelegate.get(key);
    }

    @Override
    public boolean hasKey(Object key) {
        return contextDelegate.hasKey(key);
    }

    @Override
    public int size() {
        return contextDelegate.size();
    }

    @Override
    public Stream<Map.Entry<Object, Object>> stream() {
        return contextDelegate.stream();
    }
}

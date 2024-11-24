package com.erzbir.albaz.dispatch;


import com.erzbir.albaz.common.Context;

import java.util.Map;
import java.util.stream.Stream;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public final class EventContext implements Context {
    private final Context contextDelegate = Context.contextN();
    private Event event;

    public Event getEvent() {
        return event;
    }

    void setEvent(Event event) {
        this.event = event;
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

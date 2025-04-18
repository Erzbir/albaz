package com.erzbir.albaz.dispatch.spi;

import com.erzbir.albaz.common.KVProvider;
import com.erzbir.albaz.dispatch.EventDispatcher;

import java.util.*;
import java.util.function.Supplier;

/**
 * <p>
 * 提供一个 {@link EventDispatcher} 实现
 * </p>
 *
 * @author Erzbir
 * @since 1.0.0
 */
public class EventDispatcherProvider implements KVProvider<String, EventDispatcher> {
    public static final EventDispatcherProvider INSTANCE = new EventDispatcherProvider();
    private static final Map<String, EventDispatcher> DISPATCHERS = new HashMap<>();

    static {
        ServiceLoader<EventDispatcher> loader = ServiceLoader.load(EventDispatcher.class);
        for (EventDispatcher dispatcher : loader) {
            DISPATCHERS.put(dispatcher.getClass().getName(), dispatcher);
        }
    }

    @Override
    public EventDispatcher getInstance(String key) {
        EventDispatcher dispatcher = DISPATCHERS.get(key);
        if (dispatcher == null) {
            throw new IllegalArgumentException("No EventDispatcher '" + key + "' found");
        }
        return dispatcher;
    }

    @Override
    public EventDispatcher getOrDefault(String name, Supplier<EventDispatcher> fallback) {
        return DISPATCHERS.getOrDefault(name, fallback.get());
    }

    @Override
    public Set<String> available() {
        return DISPATCHERS.keySet();
    }

    @Override
    public EventDispatcher getInstance() {
        for (EventDispatcher dispatcher : DISPATCHERS.values()) {
            return dispatcher;
        }
        throw new IllegalArgumentException("No EventDispatchers found");
    }

    @Override
    public List<EventDispatcher> getInstances() {
        return new ArrayList<>(DISPATCHERS.values());
    }
}

package com.erzbir.albaz.dispatch.internal;

import com.erzbir.albaz.dispatch.channel.EventChannel;
import com.erzbir.albaz.dispatch.event.Event;
import com.erzbir.albaz.dispatch.listener.Listener;
import com.erzbir.albaz.dispatch.listener.ListenerHandle;
import com.erzbir.albaz.dispatch.listener.ListenerStatus;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public final class GlobalEventChannel extends EventChannel<Event> {
    public static GlobalEventChannel INSTANCE = new GlobalEventChannel(Event.class);
    private final EventChannel<Event> delegate;

    private GlobalEventChannel(Class<Event> baseEventClass) {
        super(baseEventClass);
        delegate = EventChannelDispatcher.INSTANCE;
    }

    @Override
    public void broadcast(Event event) {
        delegate.broadcast(event);
    }

    @Override
    public <T extends Event> ListenerHandle registerListener(Class<T> eventType, Listener<T> listener) {
        return delegate.registerListener(eventType, listener);
    }

    @Override
    public <T extends Event> ListenerHandle subscribe(Class<T> eventType, Function<T, ListenerStatus> handle) {
        return delegate.subscribe(eventType, handle);
    }

    @Override
    public <T extends Event> ListenerHandle subscribeOnce(Class<T> eventType, Consumer<T> handle) {
        return delegate.subscribeOnce(eventType, handle);
    }

    @Override
    public <T extends Event> ListenerHandle subscribeAlways(Class<T> eventType, Consumer<T> handle) {
        return delegate.subscribeAlways(eventType, handle);
    }

    @Override
    public Listener<Event> createListener(Function<Event, ListenerStatus> handle) {
        return delegate.createListener(handle);
    }

    @Override
    public EventChannel<Event> filter(Predicate<? extends Event> predicate) {
        return delegate.filter(predicate);
    }

    @Override
    public <T extends Event> EventChannel<T> filterInstance(Class<T> eventType) {
        return delegate.filterInstance(eventType);
    }

    @Override
    public Iterable<Listener<?>> getListeners() {
        return delegate.getListeners();
    }
}



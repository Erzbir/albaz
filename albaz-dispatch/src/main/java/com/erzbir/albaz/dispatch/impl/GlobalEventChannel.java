package com.erzbir.albaz.dispatch.impl;

import com.erzbir.albaz.dispatch.channel.EventChannel;
import com.erzbir.albaz.dispatch.event.Event;
import com.erzbir.albaz.dispatch.listener.Listener;
import com.erzbir.albaz.dispatch.listener.ListenerHandle;
import com.erzbir.albaz.dispatch.listener.ListenerStatus;
import com.erzbir.albaz.dispatch.spi.GlobalEventChannelProvider;
import com.erzbir.albaz.logging.Log;
import com.erzbir.albaz.logging.LogFactory;

import java.util.ServiceLoader;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public final class GlobalEventChannel extends EventChannel<Event> {
    private static final Log log = LogFactory.getLog(GlobalEventChannel.class);
    public final static GlobalEventChannel INSTANCE = new GlobalEventChannel();
    private final EventChannel<Event> delegate;

    private GlobalEventChannel() {
        super(Event.class);
        delegate = ServiceLoader.load(GlobalEventChannelProvider.class).iterator().next().getInstance();
        if (delegate == null) {
            log.error("No GlobalEventChannelProvider found");
            throw new IllegalStateException("No GlobalChannel found");
        }
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
    public Iterable<Listener<Event>> getListeners() {
        return delegate.getListeners();
    }
}
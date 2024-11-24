package com.erzbir.albaz.dispatch.impl;


import com.erzbir.albaz.common.Interceptor;
import com.erzbir.albaz.dispatch.event.Event;
import com.erzbir.albaz.dispatch.channel.EventChannel;
import com.erzbir.albaz.dispatch.EventDispatcher;
import com.erzbir.albaz.dispatch.spi.EventDispatcherProvider;

import java.util.ServiceLoader;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public final class DefaultDispatcher implements EventDispatcher {
    public static final DefaultDispatcher INSTANCE = new DefaultDispatcher();
    private final EventDispatcher delegate = ServiceLoader.load(EventDispatcherProvider.class).iterator().next().getInstance();

    public static void main(String[] args) {
        DefaultDispatcher dispatcher = new DefaultDispatcher();
        System.out.println(dispatcher.delegate);
    }

    private DefaultDispatcher() {
    }

    @Override
    public void dispatch(Event event) {
        delegate.dispatch(event);
    }

    @Override
    public <E extends Event> void dispatch(E event, EventChannel<E> channel) {
        delegate.dispatch(event, channel);
    }

    @Override
    public void addInterceptor(Interceptor<Event> interceptor) {
        delegate.addInterceptor(interceptor);
    }

    @Override
    public void cancel() {
        delegate.cancel();
    }

    @Override
    public boolean isCanceled() {
        return delegate.isCanceled();
    }

    @Override
    public void start() {
        delegate.start();
    }

    @Override
    public boolean isActive() {
        return delegate.isActive();
    }

    @Override
    public void join() {
        delegate.join();
    }

    public enum Mode {
        POLL,
        NOTIFY
    }
}


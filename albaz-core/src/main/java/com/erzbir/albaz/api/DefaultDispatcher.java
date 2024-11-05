package com.erzbir.albaz.api;


import com.erzbir.albaz.event.EventDispatcher;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public class DefaultDispatcher implements Dispatcher {
    public static Dispatcher INSTANCE = new DefaultDispatcher();
    private final Dispatcher delegate;

    public DefaultDispatcher() {
        delegate = new InternalDispatcher();
    }

    @Override
    public DispatcherConfiguration getConfiguration() {
        return delegate.getConfiguration();
    }

    @Override
    public EventDispatcher getEventDispatcher() {
        return delegate.getEventDispatcher();
    }
}

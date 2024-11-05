package com.erzbir.albaz.event;

import com.erzbir.albaz.api.DispatcherConfiguration;

import java.util.ServiceLoader;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public class DefaultEventDispatcher implements Dispatcher {
    private static Dispatcher delegate = ServiceLoader.load(Dispatcher.class).iterator().next();

    @Override
    public DispatcherConfiguration getConfiguration() {
        return delegate.getConfiguration();
    }

    @Override
    public EventDispatcher getEventDispatcher() {
        return delegate.getEventDispatcher();
    }
}

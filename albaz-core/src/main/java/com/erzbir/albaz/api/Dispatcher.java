package com.erzbir.albaz.api;


import com.erzbir.albaz.event.EventDispatcher;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public interface Dispatcher {
    DispatcherConfiguration getConfiguration();

    EventDispatcher getEventDispatcher();
}
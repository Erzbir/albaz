package com.erzbir.albaz.event;


/**
 * @author Erzbir
 * @since 1.0.0
 */
public interface Dispatcher {
    DispatcherConfiguration getConfiguration();

    EventDispatcher getEventDispatcher();
}
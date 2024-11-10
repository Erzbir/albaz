package com.erzbir.albaz.dispatch;


/**
 * @author Erzbir
 * @since 1.0.0
 */
public interface Dispatcher {
    DispatcherConfiguration getConfiguration();

    EventDispatcher getEventDispatcher();
}
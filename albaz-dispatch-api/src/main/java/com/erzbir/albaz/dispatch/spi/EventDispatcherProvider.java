package com.erzbir.albaz.dispatch.spi;

import com.erzbir.albaz.dispatch.EventDispatcher;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public interface EventDispatcherProvider {
    EventDispatcher getInstance();
}

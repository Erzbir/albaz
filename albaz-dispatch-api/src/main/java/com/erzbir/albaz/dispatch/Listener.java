package com.erzbir.albaz.dispatch;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public interface Listener<E extends Event> {
    ListenerResult onEvent(E event);
}
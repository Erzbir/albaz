package com.erzbir.albaz.event;

/**
 * @author Erzbir
 * @since 1.0.0
 */
@SuppressWarnings("rawtypes")
public record ListenerDescription(Class<Event> eventType, Listener listener) {
}
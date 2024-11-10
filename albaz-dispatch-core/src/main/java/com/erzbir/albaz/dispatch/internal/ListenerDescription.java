package com.erzbir.albaz.dispatch.internal;

import com.erzbir.albaz.dispatch.Event;
import com.erzbir.albaz.dispatch.Listener;

/**
 * @author Erzbir
 * @since 1.0.0
 */
@SuppressWarnings("rawtypes")
public record ListenerDescription(Class<Event> eventType, Listener listener) {
}
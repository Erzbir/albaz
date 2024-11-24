package com.erzbir.albaz.dispatch;

/**
 * @author Erzbir
 * @since 1.0.0
 */
@SuppressWarnings("rawtypes")
public record InvokerContext(Event event, Listener listener) {
    public Event getEvent() {
        return event;
    }

    public Listener getListener() {
        return listener;
    }
}

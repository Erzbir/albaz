package com.erzbir.albaz.event;


import com.erzbir.albaz.common.Context;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public interface ListenerContext extends Context {
    EventContext getEventContext();

    Listener getListener();

    default Event getEvent() {
        return getEventContext().getEvent();
    }
}

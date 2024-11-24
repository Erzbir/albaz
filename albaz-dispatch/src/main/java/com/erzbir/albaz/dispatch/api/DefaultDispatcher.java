package com.erzbir.albaz.dispatch.api;


import com.erzbir.albaz.dispatch.Dispatcher;
import com.erzbir.albaz.dispatch.EventDispatcher;
import com.erzbir.albaz.dispatch.internal.NotificationEventDispatcher;
import com.erzbir.albaz.dispatch.internal.PollingEventDispatcher;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public final class DefaultDispatcher implements Dispatcher {
    private EventDispatcher eventDispatcher;
    private Mode mode;

    public DefaultDispatcher(Mode mode) {
        this.mode = mode;
    }

    @Override
    public EventDispatcher getEventDispatcher() {
        if (eventDispatcher == null) {
            switch (mode) {
                case POLL -> eventDispatcher = new PollingEventDispatcher();
                case NOTIFY -> eventDispatcher = new NotificationEventDispatcher();
                default -> eventDispatcher = new NotificationEventDispatcher();
            }
        }
        return eventDispatcher;
    }

    public enum Mode {
        POLL,
        NOTIFY
    }
}


package com.erzbir.albaz.api;


import com.erzbir.albaz.event.EventDispatcher;
import com.erzbir.albaz.event.NotificationEventDispatcher;
import com.erzbir.albaz.event.PollingEventDispatcher;

/**
 * @author Erzbir
 * @since 1.0.0
 */
class InternalDispatcher implements Dispatcher {
    private final DispatcherConfiguration config = new DefaultDispatcherConfiguration();
    private EventDispatcher eventDispatcher = new PollingEventDispatcher();

    @Override
    public DispatcherConfiguration getConfiguration() {
        return config;
    }

    @Override
    public EventDispatcher getEventDispatcher() {
        if (eventDispatcher == null) {
            switch (config.getMode()) {
                case POLL -> {
                    if (!(eventDispatcher instanceof PollingEventDispatcher)) {
                        eventDispatcher = new PollingEventDispatcher();
                    }
                }
                case NOTIFY -> {
                    if (!(eventDispatcher instanceof NotificationEventDispatcher)) {
                        eventDispatcher = new NotificationEventDispatcher();
                    }
                }
            }
        }
        return eventDispatcher;
    }
}

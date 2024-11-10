package com.erzbir.albaz.dispatch.api;


import com.erzbir.albaz.dispatch.Dispatcher;
import com.erzbir.albaz.dispatch.DispatcherConfiguration;
import com.erzbir.albaz.dispatch.EventDispatcher;
import com.erzbir.albaz.dispatch.internal.DefaultDispatcherConfiguration;
import com.erzbir.albaz.dispatch.internal.NotificationEventDispatcher;
import com.erzbir.albaz.dispatch.internal.PollingEventDispatcher;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public class DefaultDispatcher implements Dispatcher {
    private DispatcherConfiguration config = new DefaultDispatcherConfiguration();
    private EventDispatcher eventDispatcher = new PollingEventDispatcher();

    public DefaultDispatcher() {
    }

    public DefaultDispatcher(DispatcherConfiguration config) {
        this.config = config;
    }

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

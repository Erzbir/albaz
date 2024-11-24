package com.erzbir.albaz.dispatch.internal;

import com.erzbir.albaz.dispatch.EventDispatcher;
import com.erzbir.albaz.dispatch.spi.EventDispatcherProvider;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public class EventDispatcherProviderImpl implements EventDispatcherProvider {
    @Override
    public EventDispatcher getInstance() {
        return new NotificationEventDispatcher();
    }
}

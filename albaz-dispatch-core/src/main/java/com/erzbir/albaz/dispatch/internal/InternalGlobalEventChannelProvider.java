package com.erzbir.albaz.dispatch.internal;

import com.erzbir.albaz.dispatch.Event;
import com.erzbir.albaz.dispatch.EventChannel;
import com.erzbir.albaz.dispatch.GlobalEventChannelProvider;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public final class InternalGlobalEventChannelProvider implements GlobalEventChannelProvider {
    @Override
    public EventChannel<Event> getInstance() {
        return InternalGlobalEventChannel.INSTANCE;
    }
}

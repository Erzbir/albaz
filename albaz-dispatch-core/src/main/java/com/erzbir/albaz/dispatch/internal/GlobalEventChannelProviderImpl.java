package com.erzbir.albaz.dispatch.internal;

import com.erzbir.albaz.dispatch.event.Event;
import com.erzbir.albaz.dispatch.channel.EventChannel;
import com.erzbir.albaz.dispatch.spi.GlobalEventChannelProvider;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public final class GlobalEventChannelProviderImpl implements GlobalEventChannelProvider {
    @Override
    public EventChannel<Event> getInstance() {
        return GlobalEventChannel.INSTANCE;
    }
}

package com.erzbir.albaz.dispatch.spi;

import com.erzbir.albaz.dispatch.channel.EventChannel;
import com.erzbir.albaz.dispatch.event.Event;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public interface GlobalEventChannelProvider {
    EventChannel<Event> getInstance();
}

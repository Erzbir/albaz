package com.erzbir.albaz.dispatch.spi;

import com.erzbir.albaz.dispatch.channel.EventChannel;
import com.erzbir.albaz.dispatch.event.Event;

/**
 * <p>
 * 实现层需要实现这个接口提供一个 {@link EventChannel} 实现
 * </p>
 *
 * @author Erzbir
 * @since 1.0.0
 */
public interface GlobalEventChannelProvider {
    EventChannel<Event> getInstance();
}

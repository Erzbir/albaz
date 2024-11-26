package com.erzbir.albaz.dispatch.spi;

import com.erzbir.albaz.dispatch.EventDispatcher;

/**
 * <p>
 * 实现层需要实现这个接口提供一个 {@link EventDispatcher} 实现
 * </p>
 *
 * @author Erzbir
 * @since 1.0.0
 */
public interface EventDispatcherProvider {
    EventDispatcher getInstance();
}

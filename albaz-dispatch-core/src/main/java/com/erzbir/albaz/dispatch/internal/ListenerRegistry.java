package com.erzbir.albaz.dispatch.internal;

import com.erzbir.albaz.dispatch.Event;
import com.erzbir.albaz.dispatch.Listener;

/**
 * <p>
 * 监听器注册表, 记录监听器要监听的事件
 * </p>
 *
 * @author Erzbir
 * @since 1.0.0
 */
@SuppressWarnings("rawtypes")
public record ListenerRegistry(Class<Event> eventType, Listener listener) {
}
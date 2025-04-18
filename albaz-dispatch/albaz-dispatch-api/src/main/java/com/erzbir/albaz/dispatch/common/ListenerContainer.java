package com.erzbir.albaz.dispatch.common;

import com.erzbir.albaz.dispatch.event.Event;
import com.erzbir.albaz.dispatch.listener.Listener;

/**
 * 监听器容器
 *
 * @author Erzbir
 * @since 1.0.0
 */
public interface ListenerContainer {
    Iterable<Listener<Event>> getListeners();
}

package com.erzbir.albaz.dispatch;

/**
 * 监听器容器
 *
 * @author Erzbir
 * @since 1.0.0
 */
public interface ListenerContainer<E extends Event> {
    Iterable<Listener<E>> getListeners();
}

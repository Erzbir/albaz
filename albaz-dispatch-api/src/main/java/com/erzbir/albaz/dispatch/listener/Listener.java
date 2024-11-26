package com.erzbir.albaz.dispatch.listener;

import com.erzbir.albaz.dispatch.channel.EventChannel;
import com.erzbir.albaz.dispatch.event.Event;

/**
 * <p>
 * 监听器接口, 使用 {@link EventChannel} 来注册监听.
 * 注册的监听在内部会被包装成 {@code SafeListener}, 这个包装类会捕获所有错误
 * </p>
 *
 * @author Erzbir
 * @see Event
 * @see EventChannel
 * @see ListenerStatus
 * @see ConcurrencyKind
 * @since 1.0.0
 */
public interface Listener<E extends Event> {

    ListenerStatus onEvent(E event);

    /**
     * <p>
     * 获取监听器的并发类型
     * </p>
     *
     * @return {@code ConcurrencyKind}
     * @see ConcurrencyKind
     */
    default ConcurrencyKind concurrencyKind() {
        return ConcurrencyKind.CONCURRENT;
    }

    enum ConcurrencyKind {
        /**
         * 监听器被移除后可能不会立即停止监听
         */
        CONCURRENT,
        /**
         * 监听器被移除后可以立即不再监听, 通过加锁实现
         */
        LOCKED
    }

    enum TriggerType {
        INSTANT,    // 瞬时触发, 同步的方式
        CONCURRENT // 并发触发
    }

    enum Priority {
        HIGH,

        NORMAL,

        LOW,

        MONITOR
    }
}
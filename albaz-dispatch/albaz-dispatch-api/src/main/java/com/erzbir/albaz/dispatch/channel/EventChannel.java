package com.erzbir.albaz.dispatch.channel;

import com.erzbir.albaz.common.Closeable;
import com.erzbir.albaz.common.Interceptor;
import com.erzbir.albaz.dispatch.event.Event;
import com.erzbir.albaz.dispatch.listener.Listener;
import com.erzbir.albaz.dispatch.listener.ListenerHandle;
import com.erzbir.albaz.dispatch.listener.ListenerStatus;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * <p>
 * 事件通道, 是 {@link Event} 的入口, 但不负责广播事件. 提供 {@link Listener} 的注册接口.
 * </p>
 * 可通过 {@link #filter(Predicate)} 方法过滤出一个新的 {@link EventChannel}.
 * </p>
 * <p>
 * 可以通过 {@link #close()}, {@link #open()} 控制通道的开关
 * </p>
 *
 * @author Erzbir
 * @see Interceptor
 * @since 1.0.0
 */
public interface EventChannel<E extends Event> extends Closeable {
//    Class<E> getBaseEventClass();

    /**
     * <p>
     * 直接注册监听器的方法
     * </p>
     *
     * @param eventType 要监听的事件类型
     * @param listener  监听器
     * @return 监听器句柄
     * @throws IllegalArgumentException 当 eventType 或 listener 为 null 时
     * @see ListenerHandle
     * @see Listener
     */
    <T extends E> ListenerHandle registerListener(Class<T> eventType, Listener<T> listener);

    /**
     * <p>
     * 订阅一个事件
     * </p>
     *
     * @param eventType 要订阅的事件类型
     * @param handle    有一个有返回值和一个参数的 {@link Function}
     * @return 监听器句柄
     * @see ListenerHandle
     * @see Function
     * @see ListenerStatus
     */
    <T extends E> ListenerHandle subscribe(Class<T> eventType, Function<T, ListenerStatus> handle);

    /**
     * <p>
     * 订阅一个事件, 只监听一次
     * </p>
     *
     * @param eventType 要订阅的事件类型
     * @param handle    有一个参数的 {@link Consumer}
     * @return 监听器句柄
     * @see ListenerHandle
     * @see Consumer
     */
    <T extends E> ListenerHandle subscribeOnce(Class<T> eventType, Consumer<T> handle);

    /**
     * <p>
     * 订阅一个事件, 一直监听
     * </p>
     *
     * @param eventType 要订阅的事件类型
     * @param handle    有一个参数的 {@link Consumer}, 最终会被包装成一个监听器
     * @return 监听器句柄
     * @see ListenerHandle
     * @see Consumer
     */
    <T extends E> ListenerHandle subscribeAlways(Class<T> eventType, Consumer<T> handle);

    /**
     * <p>
     * 通过一个 {@link  Predicate} 来过滤出一个通道
     * </p>
     *
     * @param predicate 过滤判断条件
     * @return 过滤后的事件通道
     * @see Predicate
     */
    EventChannel<E> filter(Predicate<? super E> predicate);

    /**
     * <p>
     * 通过一个事件类型来过滤出一个通道
     * </p>
     *
     * @param eventType 事件类型
     * @return 过滤后的事件通道
     */
    <T extends E> EventChannel<T> filterInstance(Class<T> eventType);

//    Listener<E> createListener(Function<E, ListenerStatus> handle);

    void open();

    boolean isClosed();
}

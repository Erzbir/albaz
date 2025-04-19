package com.erzbir.albaz.dispatch.channel;

import com.erzbir.albaz.common.Closeable;
import com.erzbir.albaz.common.Interceptor;
import com.erzbir.albaz.dispatch.common.ListenerContainer;
import com.erzbir.albaz.dispatch.event.Event;
import com.erzbir.albaz.dispatch.listener.Listener;
import com.erzbir.albaz.dispatch.listener.ListenerHandle;
import com.erzbir.albaz.dispatch.listener.ListenerStatus;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * <p>
 * 事件通道, 提供 {@link Listener} 的注册接口, 可以注册拦截器.
 * 通过 {@link #broadcast(Event)} 方法将事件广播给所有监听器, 一个 {@link Event} 要被广播必须继承 {@link com.erzbir.albaz.dispatch.event.AbstractEvent}.
 * 可通过 {@link #filter(Predicate)} 方法过滤出一个新的 {@link EventChannel}.
 * </p>
 * <p>
 * 可以通过 {@link #close()}, {@link #open()} 控制通道的开关
 * </p>
 *
 *
 * <p>
 * 如果无需使用调度器, 可以直接使用调用 {@link #broadcast(Event)} 方法来广播事件
 * </p>
 *
 * @author Erzbir
 * @see ListenerContainer
 * @see Interceptor
 * @since 1.0.0
 */
public interface EventChannel<E extends Event> extends ListenerContainer, Closeable {
    Class<E> getBaseEventClass();

    /**
     * @param event 广播的事件
     */
    void broadcast(Event event);

    /**
     * <p>
     * 直接注册监听器的方法
     * </p>
     *
     * @param eventType 要监听的事件类型
     * @param listener  监听器
     * @return 监听器句柄
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
    EventChannel<E> filter(Predicate<? extends E> predicate);

    /**
     * <p>
     * 通过一个事件类型来过滤出一个通道
     * </p>
     *
     * @param eventType 事件类型
     * @return 过滤后的事件通道
     */
    <T extends E> EventChannel<T> filterInstance(Class<T> eventType);

    void addInterceptor(Interceptor<Listener<E>> listenerInterceptor);

    void open();

    boolean isClosed();
}

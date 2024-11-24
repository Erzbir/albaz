package com.erzbir.albaz.dispatch;


import com.erzbir.albaz.common.Interceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * <p>
 * 事件通道, 提供 监听器 的注册接口以及容器.
 * 通过 {@link  #broadcast} 方法将事件广播给所有监听器. 可通过 {@link  #filter} 方法过滤出一个新的 {@code FilterEventChannel}.
 * </p>
 *
 * <p>
 * 基于委派链的设计, 所有广播最终都会委托到 {@code EventChannelDispatcher} 中, 这种委托是链式的,
 * 例如一个 {@code   FilterEventChannel} 会将广播委托给下一个 {@code FilterEventChannel},
 * 最终再由 {@code  InternalGlobalEventChannel} 委托到 {@code EventChannelDispatcher} 中.
 * </p>
 *
 * <p>
 *     TODO 重构为用 forward 实现过滤
 * </p>
 * <p>
 *     可以注册拦截器, 拦截器在 {@link  #broadcast} 广播事件之前触发
 * </p>
 *
 * @author Erzbir
 * @see ListenerContainer
 * @see Interceptor
 * @since 1.0.0
 */
public abstract class EventChannel<E extends Event> implements ListenerContainer<E>, Cancelable {
    protected Class<E> baseEventClass;
    protected List<Interceptor<Listener<E>>> interceptors = new ArrayList<>();
    protected AtomicBoolean activated = new AtomicBoolean(true);

    public EventChannel(Class<E> baseEventClass) {
        this.baseEventClass = baseEventClass;
    }

    public Class<E> getBaseEventClass() {
        return baseEventClass;
    }

    /**
     * @param event 广播的事件
     */
    public abstract void broadcast(Event event);

    /**
     * <p>
     * 直接注册监听器的接口, {@link #subscribe}* 最终也会调用这个方法注册监听器
     * </p>
     *
     * @param eventType 要监听的事件类型
     * @param listener  监听器
     * @return 监听器句柄
     * @see ListenerHandle
     * @see Listener
     */
    public abstract <T extends E> ListenerHandle registerListener(Class<T> eventType, Listener<T> listener);

    /**
     * <p>
     * 订阅一个事件
     * </p>
     *
     * @param eventType 要订阅的事件类型
     * @param handle    有一个有返回值和一个参数的 {@link Function}, 最终会被包装成一个监听器
     * @return 监听器句柄
     * @see ListenerHandle
     * @see Function
     * @see ListenerStatus
     */
    public abstract <T extends E> ListenerHandle subscribe(Class<T> eventType, Function<T, ListenerStatus> handle);

    /**
     * <p>
     * 订阅一个事件, 只监听一次
     * </p>
     *
     * @param eventType 要订阅的事件类型
     * @param handle    有一个参数的 {@link Consumer}, 最终会被包装成一个监听器
     * @return 监听器句柄
     * @see ListenerHandle
     * @see Consumer
     */
    public abstract <T extends E> ListenerHandle subscribeOnce(Class<T> eventType, Consumer<T> handle);

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
    public abstract <T extends E> ListenerHandle subscribeAlways(Class<T> eventType, Consumer<T> handle);

    /**
     * <p>
     * 创建一个监听器. 通过这个方法可以将一个 {@link Function} 包装成一个监听器
     * </p>
     *
     * @param handle 有一个有返回值和一个参数的 {@link Function}, 最终会被包装成一个监听器
     * @return 监听器
     * @see ListenerHandle
     * @see Function
     * @see ListenerStatus
     */
    public abstract Listener<E> createListener(Function<E, ListenerStatus> handle);

    /**
     * <p>
     * 通过一个 {@link  Predicate} 来过滤出一个通道
     * </p>
     *
     * @param predicate 过滤判断条件
     * @return 过滤后的事件通道
     * @see Predicate
     */
    public abstract EventChannel<E> filter(Predicate<? extends E> predicate);

    /**
     * <p>
     * 通过一个事件类型来过滤出一个通道
     * </p>
     *
     * @param eventType 事件类型
     * @return 过滤后的事件通道
     */
    public abstract <T extends E> EventChannel<T> filterInstance(Class<T> eventType);

//    public abstract <T extends E> void forward(EventChannel<E> channel);

//    protected abstract void callListeners(E event);

    public void addInterceptor(Interceptor<Listener<E>> listenerInterceptor) {
        interceptors.add(listenerInterceptor);
    }

    public void close() {
        activated.set(false);
    }

    public void close(Runnable hook, boolean isAsync) {
        if (isAsync) {
            CompletableFuture.runAsync(hook);
        } else {
            hook.run();
        }
    }

    public void open() {
        activated.set(true);
    }

    @Override
    public void cancel() {
        close();
    }

    @Override
    public boolean isCanceled() {
        return !activated.get();
    }
}

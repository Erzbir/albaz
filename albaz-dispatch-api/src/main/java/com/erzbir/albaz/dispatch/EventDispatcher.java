package com.erzbir.albaz.dispatch;


import com.erzbir.albaz.interceptor.DispatchInterceptor;

/**
 * 事件调度器
 *
 * @author Erzbir
 * @since 1.0.0
 */
public interface EventDispatcher extends Cancelable {

    default void dispatch(Event event) {
        dispatch(event, GlobalEventChannel.INSTANCE);
    }

    <E extends Event> void dispatch(E event, EventChannel<E> channel);

    void addInterceptor(DispatchInterceptor dispatchInterceptor);

    void start();

    boolean isActive();

    void join();

}

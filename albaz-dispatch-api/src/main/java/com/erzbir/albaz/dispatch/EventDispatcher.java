package com.erzbir.albaz.dispatch;


import com.erzbir.albaz.common.Interceptor;
import com.erzbir.albaz.dispatch.channel.EventChannel;
import com.erzbir.albaz.dispatch.common.Cancelable;
import com.erzbir.albaz.dispatch.event.Event;

/**
 * 事件调度器
 *
 * @author Erzbir
 * @since 1.0.0
 */
public interface EventDispatcher extends Dispatcher, Cancelable {
    void dispatch(Event event);

    <E extends Event> void dispatch(E event, EventChannel<E> channel);

    void addInterceptor(Interceptor<Event> interceptor);
}

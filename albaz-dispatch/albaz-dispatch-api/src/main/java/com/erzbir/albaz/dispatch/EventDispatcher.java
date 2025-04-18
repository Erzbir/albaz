package com.erzbir.albaz.dispatch;


import com.erzbir.albaz.common.Interceptor;
import com.erzbir.albaz.dispatch.channel.EventChannel;
import com.erzbir.albaz.dispatch.common.Cancelable;
import com.erzbir.albaz.dispatch.event.Event;

/**
 * <p>
 * 事件调度器, 事件通过这个接口分发到指定的 {@link EventChannel}
 * </p>
 *
 * @author Erzbir
 * @see Dispatcher
 * @since 1.0.0
 */
public interface EventDispatcher extends Dispatcher, Cancelable {
    void dispatch(Event event);

    <E extends Event> void dispatch(E event, EventChannel<E> channel);

    void addInterceptor(Interceptor<Event> interceptor);

    EventChannel<Event> getEventChannel();
}

package com.erzbir.albaz.dispatch.internal;


import com.erzbir.albaz.dispatch.Event;
import com.erzbir.albaz.dispatch.EventContext;

/**
 * 事件的广播, 订阅等都委托到此处处理
 *
 * @author Erzbir
 * @since 1.0.0
 */
class EventChannelDispatcher<E extends Event> extends EventChannelImpl<E> {
    public static final EventChannelDispatcher<Event> INSTANCE = new EventChannelDispatcher<>(Event.class);

    private EventChannelDispatcher(Class<E> baseEventClass) {
        super(baseEventClass);
    }

    @Override
    public void broadcast(EventContext eventContext) {
        super.broadcast(eventContext);
    }
}

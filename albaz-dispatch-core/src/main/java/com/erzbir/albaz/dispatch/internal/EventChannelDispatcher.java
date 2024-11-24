package com.erzbir.albaz.dispatch.internal;


import com.erzbir.albaz.dispatch.event.Event;

/**
 * 事件的广播, 订阅等都委托到此处处理
 *
 * @author Erzbir
 * @since 1.0.0
 */
public final class EventChannelDispatcher<E extends Event> extends EventChannelImpl<E> {
    public static final EventChannelDispatcher<Event> INSTANCE = new EventChannelDispatcher<>(Event.class);

    private EventChannelDispatcher(Class<E> baseEventClass) {
        super(baseEventClass);
    }

    @Override
    public void broadcast(Event event) {
        super.broadcast(event);
    }
}

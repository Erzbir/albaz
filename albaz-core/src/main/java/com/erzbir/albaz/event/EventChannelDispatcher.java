package com.erzbir.albaz.event;


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
    protected void broadcast(EventContext eventContext) {
        super.broadcast(eventContext);
    }
}

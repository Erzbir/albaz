package com.erzbir.albaz.dispatch.internal;

import com.erzbir.albaz.dispatch.channel.EventChannel;
import com.erzbir.albaz.dispatch.event.Event;
import com.erzbir.albaz.dispatch.listener.Listener;
import com.erzbir.albaz.dispatch.listener.ListenerHandle;
import com.erzbir.albaz.dispatch.listener.ListenerStatus;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Erzbir
 * @since 1.0.0
 */
final class FilterEventChannel<E extends Event> extends EventChannel<E> {
    private final EventChannel<E> delegate;
    private final Predicate<Event> filter;

    public FilterEventChannel(EventChannel<E> delegate, Predicate<Event> filter) {
        super(delegate.getBaseEventClass());
        this.delegate = delegate;
        this.filter = filter;
    }


    public FilterEventChannel(EventChannel<E> delegate, Class<E> eventType) {
        this(delegate, eventType::isInstance);
        this.baseEventClass = eventType;
    }


    @Override
    public void broadcast(Event event) {
        if (!baseEventClass.isInstance(event) && filter.test(event)) {
            return;
        }
        delegate.broadcast(event);
    }

    @Override
    public <T extends E> ListenerHandle registerListener(Class<T> eventType, Listener<T> listener) {
        return delegate.registerListener(eventType, intercept(listener));
    }

    @Override
    public <T extends E> ListenerHandle subscribe(Class<T> eventType, Function<T, ListenerStatus> handler) {
        return delegate.subscribe(eventType, intercept(handler));
    }

    @Override
    public <T extends E> ListenerHandle subscribeOnce(Class<T> eventType, Consumer<T> handler) {
        return delegate.subscribeOnce(eventType, intercept(handler));
    }

    @Override
    public <T extends E> ListenerHandle subscribeAlways(Class<T> eventType, Consumer<T> handler) {
        return delegate.subscribeAlways(eventType, intercept(handler));
    }

    @Override
    public Listener<E> createListener(Function<E, ListenerStatus> handler) {
        return delegate.createListener(intercept(handler));
    }

    private <T extends E> Listener<T> intercept(Listener<T> listener) {
        return (ev) -> {
            boolean filterResult;
            filterResult = getBaseEventClass().isInstance(ev) && filter.test(ev);
            if (filterResult) {
                return listener.onEvent(ev);
            } else {
                return ListenerStatus.TRUNCATED;
            }
        };
    }

    /**
     * 这里重写了父类是为了形成一条链, 因为从一个 {@link FilterEventChannel} 过滤的通道, {@link #delegate} 就必须是原本的 {@link FilterEventChannel}.
     * 父类实现的方法只是为每一个通道都提供一个可以过滤得到 {@link FilterEventChannel} 的方式.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public EventChannel<E> filter(Predicate<? extends E> predicate) {
        return new FilterEventChannel(this, predicate);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public <T extends E> EventChannel<T> filterInstance(Class<T> eventType) {
        return new FilterEventChannel(this, eventType);
    }

    @Override
    public Iterable<Listener<Event>> getListeners() {
        return delegate.getListeners();
    }

    private <T extends E> Consumer<T> intercept(Consumer<T> handler) {
        return (ev) -> {
            boolean filterResult;
            filterResult = getBaseEventClass().isInstance(ev) && filter.test(ev);
            if (filterResult) {
                handler.accept(ev);
            }
        };
    }

    private <T extends E> Function<T, ListenerStatus> intercept(Function<T, ListenerStatus> handler) {
        return (ev) -> {
            boolean filterResult;
            filterResult = getBaseEventClass().isInstance(ev) && filter.test(ev);
            if (filterResult) {
                return handler.apply(ev);
            } else {
                return ListenerStatus.TRUNCATED;
            }
        };
    }

}

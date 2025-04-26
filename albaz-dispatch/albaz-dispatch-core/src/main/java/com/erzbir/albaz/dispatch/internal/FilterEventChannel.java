package com.erzbir.albaz.dispatch.internal;

import com.erzbir.albaz.dispatch.channel.EventChannel;
import com.erzbir.albaz.dispatch.event.Event;
import com.erzbir.albaz.dispatch.listener.Listener;
import com.erzbir.albaz.dispatch.listener.ListenerHandle;
import com.erzbir.albaz.dispatch.listener.ListenerStatus;
import com.erzbir.albaz.logging.Log;
import com.erzbir.albaz.logging.LogFactory;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * <p>过滤通道, 是一个子通道. 关闭子通道不会影响父通道, 父通道关闭同样也不会关闭子通道, 但打开子通道会打开父通道</p>
 * <p>过滤是惰性的, 发生在创建监听器时, 在没有执行 {@link Listener#onEvent(Event)} 之前过滤都不会发生</p>
 * <p>因为过滤是惰性的, 所以被过滤的 {@link Event} 会传递到监听器中, 但是监听器不会处理这个事件</p>
 *
 * @author Erzbir
 * @since 1.0.0
 */
final class FilterEventChannel<E extends Event> extends AbstractEventChannel<E> {
    private final AbstractEventChannel<E> delegate;
    private final Predicate<E> filter;
    private final Log log = LogFactory.getLog(FilterEventChannel.class);

    FilterEventChannel(AbstractEventChannel<E> delegate, Predicate<E> filter) {
        super(delegate.baseEventClass);
        this.delegate = delegate;
        this.filter = filter;
    }

    FilterEventChannel(AbstractEventChannel<E> delegate, Class<E> eventType) {
        this(delegate, eventType::isInstance);
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

    /**
     * 这里重写了父类是为了形成一条链, 因为从一个 {@link FilterEventChannel} 过滤的通道, {@link #delegate} 就必须是原本的 {@link FilterEventChannel}.
     * 父类实现的方法只是为每一个通道都提供一个可以过滤得到 {@link FilterEventChannel} 的方式.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public EventChannel<E> filter(Predicate<? super E> predicate) {
        return new FilterEventChannel(this, predicate);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public <T extends E> EventChannel<T> filterInstance(Class<T> eventType) {
        return new FilterEventChannel(this, eventType);
    }

    private boolean shouldFilter(E event) {
        return isClosed() || !baseEventClass.isAssignableFrom(event.getClass()) || !filter.test(event);
    }

    private <T extends E> Listener<T> intercept(Listener<T> listener) {
        return (ev) -> {
            if (!shouldFilter(ev)) {
                return listener.onEvent(ev);
            } else {
                log.debug("Filtered event: {} in {}", ev, this.getClass().getName());
                return ListenerStatus.CONTINUE;
            }
        };
    }

    private <T extends E> Consumer<T> intercept(Consumer<T> handler) {
        return (ev) -> {
            if (!shouldFilter(ev)) {
                handler.accept(ev);
            } else {
                log.debug("Filtered {} in {}", ev, this.getClass().getName());
            }
        };
    }

    private <T extends E> Function<T, ListenerStatus> intercept(Function<T, ListenerStatus> handler) {
        return (ev) -> {
            if (!shouldFilter(ev)) {
                return handler.apply(ev);
            } else {
                log.debug("Filtered event: {} in {}", ev, this.getClass().getName());
                return ListenerStatus.CONTINUE;
            }
        };
    }

    @Override
    public void open() {
        super.open();
        delegate.open();
    }
}

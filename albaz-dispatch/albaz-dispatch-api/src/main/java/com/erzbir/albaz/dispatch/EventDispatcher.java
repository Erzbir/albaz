package com.erzbir.albaz.dispatch;


import com.erzbir.albaz.common.Interceptor;
import com.erzbir.albaz.dispatch.channel.EventChannel;
import com.erzbir.albaz.dispatch.event.Event;

/**
 * <p>事件调度器, 通过这个接口将 {@link Event} 分发到 {@link EventChannel} 中, 调度器需要处于 {@code active} 状态 ({@link #isActive()}, {@link #start()})</p>
 *
 * <p>通过 {@link #getEventChannel()} 可以获取一个默认分发的 {@link EventChannel}</p>
 * <p>分发一个 {@link Event}:
 * <ul>
 *     <li>由调度器自己选择 {@link EventChannel}: {@link #dispatch(Event)}</li>
 *     <li>指定一个 {@link EventChannel}: {@link #dispatch(Event)}</li>
 * </ul>
 * </p>
 *
 * <p>调用 {@link #close()} 不可以继续分发 {@link Event}, 必须要调用 {@link #start()} 之后才可继续</p>
 *
 * <p>{@link #join()} {@link #join(long)} {@link #await()} 默认没有实现, 这是提供给异步使用的</p>
 *
 * @author Erzbir
 * @see Dispatcher
 * @see EventChannel
 * @see Event
 * @since 1.0.0
 */
public interface EventDispatcher extends Dispatcher {
    void dispatch(Event event);

    void addInterceptor(Interceptor<? extends Event> interceptor);

    EventChannel<Event> getEventChannel();

    default void join() {
    }

    default void join(long timeout) {
    }

    default void await() {
    }

    default AsyncEventDispatcher async() throws NotAsyncDispatcherException {
        if (this instanceof AsyncEventDispatcher) {
            return (AsyncEventDispatcher) this;
        }
        throw new NotAsyncDispatcherException(String.format("This EventDispatcher: %s does not support async mode", this));
    }
}
package com.erzbir.albaz.dispatch;

import com.erzbir.albaz.dispatch.event.Event;

import java.util.concurrent.CompletableFuture;

/**
 * <p>
 * 一个异步的 {@link EventDispatcher}
 * </p>
 *
 * @author Erzbir
 * @see EventDispatcher
 * @since 1.0.0
 */
public interface AsyncEventDispatcher extends EventDispatcher {
    static AsyncEventDispatcher of(EventDispatcher dispatcher) throws NotAsyncDispatcherException {
        if (dispatcher instanceof AsyncEventDispatcher async) return async;
        throw new NotAsyncDispatcherException(String.format("This EventDispatcher: %s does not support async mode", dispatcher));
    }

    CompletableFuture<Void> dispatchAsync(Event event);

    /**
     * 阻塞当前调用线程
     */
    void join();

    void join(long timeout);

    /**
     * 等待任务全部结束, 不会阻塞调用线程
     */
    void await();
}

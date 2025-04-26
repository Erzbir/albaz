package com.erzbir.albaz.dispatch.internal;

import com.erzbir.albaz.common.Interceptor;
import com.erzbir.albaz.dispatch.AsyncEventDispatcher;
import com.erzbir.albaz.dispatch.EventDispatcher;
import com.erzbir.albaz.dispatch.channel.EventChannel;
import com.erzbir.albaz.dispatch.event.Event;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p>
 * {@link EventDispatcher} 被这个类包装了之后就会变成一个 {@link AsyncEventDispatcher}.
 * 即使原本同步的 {@link #dispatch(Event)} 也会转为异步, 但只有 {@link #dispatchAsync(Event)} 才返回 {@link CompletableFuture} 对象
 * </p>
 *
 * @author Erzbir
 * @since 1.0.0
 */
class AsyncDispatcherWrapper implements AsyncEventDispatcher {
    private final ExecutorService dispatchThreadPool;
    private final EventDispatcher delegate;

    public AsyncDispatcherWrapper(EventDispatcher delegate) {
        this.delegate = delegate;
        this.dispatchThreadPool = Executors.newThreadPerTaskExecutor(Thread.ofVirtual().name("Dispatch-Thread-", 0).factory());
    }

    public AsyncDispatcherWrapper(EventDispatcher delegate, ExecutorService dispatchThreadPool) {
        this.delegate = delegate;
        this.dispatchThreadPool = dispatchThreadPool;
    }

    @Override
    public void dispatch(Event event) {
        dispatchAsync(event);
    }


    @Override
    public void addInterceptor(Interceptor<? extends Event> interceptor) {
        delegate.addInterceptor(interceptor);
    }

    @Override
    public EventChannel<Event> getEventChannel() {
        return delegate.getEventChannel();
    }

    @Override
    public AsyncEventDispatcher async() {
        return this;
    }

    @Override
    public void start() {
        delegate.start();
    }

    @Override
    public boolean isActive() {
        return delegate.isActive();
    }

    @Override
    public void close() {
        delegate.close();
    }

    @Override
    public CompletableFuture<Void> dispatchAsync(Event event) {
        if (delegate instanceof AsyncEventDispatcher async) {
            async.dispatchAsync(event);
        }
        return CompletableFuture.runAsync(() -> delegate.dispatch(event), dispatchThreadPool);
    }

    @Override
    public void join() {
        delegate.join();
    }

    @Override
    public void join(long timeout) {
        delegate.join(timeout);
    }

    @Override
    public void await() {
        delegate.await();
    }
}
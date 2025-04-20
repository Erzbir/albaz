package com.erzbir.albaz.dispatch.internal;

import com.erzbir.albaz.dispatch.AsyncEventDispatcher;
import com.erzbir.albaz.dispatch.EventDispatcher;
import com.erzbir.albaz.dispatch.channel.EventChannel;
import com.erzbir.albaz.dispatch.event.CancelableEvent;
import com.erzbir.albaz.dispatch.event.Event;
import com.erzbir.albaz.logging.Log;
import com.erzbir.albaz.logging.LogFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 基于通知的 {@link EventDispatcher}
 *
 * @author Erzbir
 * @since 1.0.0
 */
public final class NotificationEventDispatcher extends AbstractEventDispatcher implements AsyncEventDispatcher {
    private final Log log = LogFactory.getLog(NotificationEventDispatcher.class);
    private final Object lock = new Object();
    private final AtomicInteger count = new AtomicInteger(0);
    private final ExecutorService dispatchThreadPool = Executors.newThreadPerTaskExecutor(Thread.ofVirtual().name("Dispatch-Thread-", 0).factory());
    private Thread guardThread;

    public NotificationEventDispatcher() {
        start();
    }

    @Override
    protected <E extends Event> void dispatchTo(E event, EventChannel<E> channel) {
        createDispatchTask(channel, event).run();
    }

    @Override
    public <E extends Event> CompletableFuture<Void> dispatchAsync(E event, EventChannel<E> channel) {
        return CompletableFuture.runAsync(() -> dispatch(event, channel), dispatchThreadPool);
    }

    @Override
    public CompletableFuture<Void> dispatchAsync(Event event) {
        return dispatchAsync(event, globalEventChannel);
    }


    private <E extends Event> void runDispatchTask(EventChannel<E> channel, E event) {
        try {
            if (event instanceof CancelableEvent cancelableEvent) {
                if (cancelableEvent.isCanceled()) {
                    return;
                }
            }
            EventChannelDispatcher.INSTANCE.broadcast(event);
        } catch (Throwable e) {
            log.error("Dispatching to channel: " + channel.getClass().getName() + " error: " + e.getMessage());
        }
    }

    private <E extends Event> Runnable createDispatchTask(EventChannel<E> channel, E event) {
        count.getAndIncrement();
        return () -> {
            try {
                runDispatchTask(channel, event);
            } finally {
                taskCompleted();
            }
        };
    }

    private void taskCompleted() {
        int remaining = count.decrementAndGet();
        if (remaining == 0) {
            synchronized (lock) {
                lock.notifyAll();
            }
        }
    }

    @Override
    public void start() {
        activated.set(true);
    }

    @Override
    public void join() {
        join(0L);
    }

    @Override
    public void join(long timeout) {
        synchronized (lock) {
            try {
                while (count.get() > 0) {
                    lock.wait(timeout);
                }
            } catch (InterruptedException e) {
                log.error("Dispatching error when join thread: " + Thread.currentThread().getName());
            }
        }
    }

    @Override
    public void await() {
        guardThread = new Thread(this::join, "Dispatch-Thread-Guard");
        guardThread.start();
    }

    @Override
    public AsyncEventDispatcher async() {
        return new AsyncDispatcherWrapper(this, dispatchThreadPool);
    }

    @Override
    public void close() {
        if (!isActive()) {
            log.warn("EventDispatcher: " + getClass().getSimpleName() + " is already closed");
            return;
        }
        activated.set(false);

        synchronized (lock) {
            lock.notifyAll();
        }
        if (guardThread != null) {
            guardThread.interrupt();
            guardThread = null;
        }
    }
}

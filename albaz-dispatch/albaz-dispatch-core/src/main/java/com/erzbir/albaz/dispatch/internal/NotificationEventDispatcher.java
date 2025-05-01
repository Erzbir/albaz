package com.erzbir.albaz.dispatch.internal;

import com.erzbir.albaz.dispatch.AsyncEventDispatcher;
import com.erzbir.albaz.dispatch.EventDispatcher;
import com.erzbir.albaz.dispatch.event.CancelableEvent;
import com.erzbir.albaz.dispatch.event.Event;
import com.erzbir.albaz.logging.Log;
import com.erzbir.albaz.logging.LogFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
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
    private ExecutorService dispatchThreadPool = Executors.newThreadPerTaskExecutor(Thread.ofVirtual().name("Dispatch-Thread-", 0).factory());
    private Thread guardThread;

    public NotificationEventDispatcher() {
        start();
    }

    @Override
    protected void dispatchTo(Event event) {
        createDispatchTask(event).run();
    }

    @Override
    public CompletableFuture<Void> dispatchAsync(Event event) {
        if (event == null) {
            throw new IllegalArgumentException("Event must not be null");
        }
        return CompletableFuture.runAsync(() -> dispatch(event), dispatchThreadPool).whenComplete((res, ex) -> log.error("Dispatching event: [{}] failed, [{}]", event, ex));
    }

    private <E extends Event> void runDispatchTask(E event) {
        try {
            if (event instanceof CancelableEvent cancelableEvent) {
                if (cancelableEvent.isCanceled()) {
                    return;
                }
            }
            globalEventChannel.broadcast(event);
        } catch (Throwable e) {
            log.error("Dispatching to channel: [{}] error: [{}]", getEventChannel().getClass().getName(), e);
            Thread.currentThread().interrupt();
        }
    }

    private <E extends Event> Runnable createDispatchTask(E event) {
        count.getAndIncrement();
        return () -> {
            try {
                runDispatchTask(event);
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
        if (isActive()) {
            log.warn("EventDispatcher: [{}] is already started, can't start again", getClass().getSimpleName());
            return;
        }
        activated.set(true);
        dispatchThreadPool = Executors.newThreadPerTaskExecutor(Thread.ofVirtual().name("Dispatch-Thread-", 0).factory());
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
                Thread.currentThread().interrupt();
                log.error("Dispatching error when join thread: [{}]", Thread.currentThread().getName());
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
            log.warn("EventDispatcher: [{}] is already closed, can't close again", getClass().getSimpleName());
            return;
        }
        activated.set(false);

        synchronized (lock) {
            lock.notifyAll();
        }

        ExecutorServiceShutdownHelper.shutdown(dispatchThreadPool, 100, TimeUnit.MILLISECONDS);

        if (guardThread != null) {
            guardThread.interrupt();
            guardThread = null;
        }
    }
}

package com.erzbir.albaz.dispatch.internal;

import com.erzbir.albaz.dispatch.EventDispatcher;
import com.erzbir.albaz.dispatch.channel.EventChannel;
import com.erzbir.albaz.dispatch.event.CancelableEvent;
import com.erzbir.albaz.dispatch.event.Event;
import com.erzbir.albaz.logging.Log;
import com.erzbir.albaz.logging.LogFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 基于通知的 {@link EventDispatcher}
 *
 * @author Erzbir
 * @since 1.0.0
 */
public final class NotificationEventDispatcher extends AbstractEventDispatcher implements EventDispatcher {
    private final Log log = LogFactory.getLog(NotificationEventDispatcher.class);
    private final Object lock = new Object();
    private final AtomicInteger count = new AtomicInteger(0);
    private Thread guardThread;

    @Override
    protected <E extends Event> void dispatchTo(E event, EventChannel<E> channel) {
        Thread.ofVirtual()
                .name("Dispatch-Thread-" + Thread.currentThread().threadId())
                .start(createDispatchTask(channel, event));
    }

    private <E extends Event> Runnable createDispatchTask(EventChannel<E> channel, E event) {
        count.getAndIncrement();
        return () -> {
            try {
                if (event instanceof CancelableEvent cancelableEvent) {
                    if (cancelableEvent.isCanceled()) {
                        return;
                    }
                }
                log.debug("Dispatching event:" + event + " to channel: " + channel.getClass().getSimpleName());
                channel.broadcast(event);
            } catch (Throwable e) {
                log.error("Dispatching to channel: " + channel.getClass().getSimpleName() + " error: " + e.getMessage());
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
    public void cancel() {
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

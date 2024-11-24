package com.erzbir.albaz.dispatch.internal;

import com.erzbir.albaz.dispatch.EventDispatcher;
import com.erzbir.albaz.dispatch.channel.EventChannel;
import com.erzbir.albaz.dispatch.event.CancelableEvent;
import com.erzbir.albaz.dispatch.event.Event;
import com.erzbir.albaz.logging.Log;
import com.erzbir.albaz.logging.LogFactory;

/**
 * 基于通知的 {@link EventDispatcher}
 *
 * @author Erzbir
 * @since 1.0.0
 */
public final class NotificationEventDispatcher extends AbstractEventDispatcher implements EventDispatcher {
    private final Log log = LogFactory.getLog(NotificationEventDispatcher.class);
    private final Object lock = new Object();
    private volatile int count = 0;
    private Thread guardThread;

    @Override
    protected <E extends Event> void dispatchTo(E event, EventChannel<E> channel) {
        synchronized (lock) {
            count++;
        }
        Thread.ofVirtual()
                .name("Dispatch-Thread-" + Thread.currentThread().threadId())
                .start(createDispatchTask(channel, event));
    }

    private <E extends Event> Runnable createDispatchTask(EventChannel<E> channel, E event) {
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
                synchronized (lock) {
                    count--;
                }
            }
        };
    }

    @Override
    public void join() {
        try {
            while (count > 0) {
                Thread.sleep(10);
            }
        } catch (InterruptedException e) {
            log.error("Dispatching error when join thread: " + guardThread);
        }
    }

    // TODO impl
    @Override
    public void join(long timeout) {

    }

    @Override
    public void await() {
        guardThread = new Thread(this::join, "Dispatch-Thread-Guard");
        guardThread.start();
    }

    // TODO impl
    @Override
    public void await(long timeout) {

    }

    @Override
    public void cancel() {
        super.cancel();
        synchronized (lock) {
            lock.notifyAll();
        }
        guardThread.interrupt();
    }
}

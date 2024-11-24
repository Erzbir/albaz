package com.erzbir.albaz.dispatch.internal;

import com.erzbir.albaz.dispatch.CancelableEvent;
import com.erzbir.albaz.dispatch.Event;
import com.erzbir.albaz.dispatch.EventChannel;
import com.erzbir.albaz.dispatch.EventDispatcher;
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

    @Override
    protected <E extends Event> void dispatchTo(E event, EventChannel<E> channel) {
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
            }
        };
    }

    @Override
    public void join() {

    }

    @Override
    public void cancel() {
        super.cancel();
    }
}

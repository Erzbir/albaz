package com.erzbir.albaz.dispatch.internal;

import com.erzbir.albaz.dispatch.CancelableEvent;
import com.erzbir.albaz.dispatch.Event;
import com.erzbir.albaz.dispatch.EventChannel;
import com.erzbir.albaz.dispatch.EventDispatcher;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 基于通知的 {@link EventDispatcher}
 *
 * @author Erzbir
 * @since 1.0.0
 */
@Slf4j
public class NotificationEventDispatcher extends AbstractEventDispatcher implements EventDispatcher {
    private final List<Thread> threads = new ArrayList<>();

    @Override
    protected <E extends Event> void dispatchTo(E event, EventChannel<E> channel) {
        Thread thread = Thread.ofVirtual()
                .name("Dispatcher-Thread")
                .start(createTask(channel, event));
        threads.add(thread);
    }

    private <E extends Event> Runnable createTask(EventChannel<E> channel, E event) {
        return () -> {
            try {
                if (event instanceof CancelableEvent cancelableEvent) {
                    if (cancelableEvent.isCanceled()) {
                        return;
                    }
                }
                log.debug("Dispatching event: {} to channel: {}", event, channel.getClass().getSimpleName());
                channel.broadcast(new DefaultEventContext(event));
            } catch (Throwable e) {
                log.error("Dispatching to channel: {} error: {}", channel.getClass().getSimpleName(), e.getMessage());
                Thread.currentThread().interrupt();
            }
        };
    }

    @Override
    public void join() {
        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                log.error("Dispatching interrupted error: {}", e.getMessage());
            }
        });
    }

    @Override
    public void cancel() {
        super.cancel();
        threads.clear();
    }
}

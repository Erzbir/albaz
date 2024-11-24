package com.erzbir.albaz.dispatch.internal;

import org.junit.jupiter.api.Test;

/**
 * @author Erzbir
 * @since 1.0.0
 */
class NotificationEventDispatcherTest {

    @Test
    void dispatch() throws InterruptedException {
        NotificationEventDispatcher dispatcher = new NotificationEventDispatcher();
        dispatcher.start();
        dispatcher.dispatch(new TestEvent(this));
        Thread.sleep(1000);
    }

    @Test
    void cancel() throws InterruptedException {
        NotificationEventDispatcher dispatcher = new NotificationEventDispatcher();
        dispatcher.start();
        Thread thread = new Thread(() -> {
            int i = 10;
            while (i-- > 0) {
                dispatcher.dispatch(new TestEvent(this));
            }
        });
        thread.start();
        Thread.sleep(1000);
        dispatcher.cancel();
        int i = 10;
        while (i-- > 0) {
            dispatcher.dispatch(new TestEvent(this));
        }
    }

    @Test
    void start() {
        NotificationEventDispatcher dispatcher = new NotificationEventDispatcher();
        dispatcher.dispatch(new TestEvent(this));
        dispatcher.start();
        dispatcher.dispatch(new TestEvent(this));
    }
}
package com.erzbir.albaz.dispatch.internal;

import org.junit.jupiter.api.Test;

class NotificationEventDispatcherTest {

    @Test
    void dispatch() throws InterruptedException {
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
        dispatcher.join();
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
        dispatcher.join();
        dispatcher.cancel();
    }
}
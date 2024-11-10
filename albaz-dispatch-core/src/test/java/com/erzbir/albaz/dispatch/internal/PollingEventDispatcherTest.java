package com.erzbir.albaz.dispatch.internal;

import org.junit.jupiter.api.Test;

class PollingEventDispatcherTest {

    @Test
    void dispatch() throws InterruptedException {
        PollingEventDispatcher dispatcher = new PollingEventDispatcher();
        dispatcher.start();
        Thread thread = new Thread(() -> {
            int i = 10;
            while (i-- > 0) {
                dispatcher.dispatch(new TestEvent(this));
            }
        });
        thread.start();
        Thread.sleep(100);
        dispatcher.cancel();
        dispatcher.join();
    }

    @Test
    void cancel() throws InterruptedException {
        PollingEventDispatcher dispatcher = new PollingEventDispatcher();
        dispatcher.start();
        Thread thread = new Thread(() -> {
            int i = 10;
            while (i-- > 0) {
                dispatcher.dispatch(new TestEvent(this));
            }
        });
        thread.start();
        Thread.sleep(1000);
//        dispatcher.join();
        dispatcher.cancel();
    }
}
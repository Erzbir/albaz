package com.erzbir.albaz.dispatch.internal;

import org.junit.jupiter.api.Test;

/**
 * @author Erzbir
 * @since 1.0.0
 */
class PollingEventDispatcherTest {

    public static void main(String[] args) {
        PollingEventDispatcherTest test = new PollingEventDispatcherTest();
        PollingEventDispatcher dispatcher = new PollingEventDispatcher();
        dispatcher.start();
        test.dispatchBatch(dispatcher);
        dispatcher.join();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("122");
        dispatcher.cancel();
    }

    private void dispatchBatch(PollingEventDispatcher dispatcher) {
        int i = 10;
        while (i-- > 0) {
            new Thread(() -> dispatcher.dispatch(new TestEvent(this))).start();
        }
    }

    @Test
    void dispatch() throws InterruptedException {
        PollingEventDispatcher dispatcher = new PollingEventDispatcher();
        dispatcher.start();
        dispatcher.dispatch(new TestEvent(this));
        Thread.sleep(100);
    }

    @Test
    void cancel() throws InterruptedException {
        PollingEventDispatcher dispatcher = new PollingEventDispatcher();
        dispatcher.start();
        dispatchBatch(dispatcher);
        dispatcher.cancel();
        Thread.sleep(100);
    }

    @Test
    void join() throws InterruptedException {
        PollingEventDispatcher dispatcher = new PollingEventDispatcher();
        dispatcher.start();
        dispatchBatch(dispatcher);
        dispatcher.join();
        Thread.sleep(100);
        System.out.println("122");
    }

    @Test
    void start() {
        PollingEventDispatcher dispatcher = new PollingEventDispatcher();
        dispatcher.start();
    }
}
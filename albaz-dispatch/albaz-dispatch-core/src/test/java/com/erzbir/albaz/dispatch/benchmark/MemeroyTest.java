package com.erzbir.albaz.dispatch.benchmark;

import com.erzbir.albaz.dispatch.AsyncEventDispatcher;
import com.erzbir.albaz.dispatch.internal.NotificationEventDispatcher;
import com.erzbir.albaz.dispatch.internal.TestEvent;
import com.erzbir.albaz.dispatch.listener.Listener;
import com.erzbir.albaz.dispatch.listener.ListenerStatus;

import java.util.concurrent.TimeUnit;

/**
 * @author Erzbir
 * @since 1.0.0
 */
class PerfTest {

    public static void main(String[] args) throws InterruptedException {
        PerfTest perfTest = new PerfTest();
        perfTest.test();
    }

    public void test() throws InterruptedException {
        AsyncEventDispatcher dispatcher = new NotificationEventDispatcher();
        dispatcher.start();

        Listener<TestEvent> listener = event -> {
            new CPUTask(10000).run();
            return ListenerStatus.CONTINUE;
        };

        Listener<TestEvent> listener1 = new Listener<>() {
            @Override
            public ListenerStatus onEvent(TestEvent event) {
                new IOTask("test-io.txt").run();
                return ListenerStatus.CONTINUE;
            }

            @Override
            public TriggerType triggerType() {
                return TriggerType.CONCURRENT;
            }

            @Override
            public ConcurrencyKind concurrencyKind() {
                return ConcurrencyKind.CONCURRENT;
            }
        };

        dispatcher.getEventChannel().registerListener(TestEvent.class, listener);
        dispatcher.getEventChannel().registerListener(TestEvent.class, listener1);

        Thread deadLoopThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                dispatcher.dispatch(new TestEvent(this));
                try {
                    TimeUnit.MILLISECONDS.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        deadLoopThread.start();
        TimeUnit.SECONDS.sleep(30);
        deadLoopThread.interrupt();
        dispatcher.close();
    }
}
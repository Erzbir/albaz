package com.erzbir.albaz.dispatch.api;

import com.erzbir.albaz.dispatch.EventDispatcher;
import com.erzbir.albaz.dispatch.GlobalEventChannel;
import com.erzbir.albaz.dispatch.ListenerStatus;
import com.erzbir.albaz.dispatch.internal.PollingEventDispatcher;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class EventDispatcherThroughputTester {

    private final AtomicLong totalExecutionTime = new AtomicLong(0);
    private final AtomicLong eventsProcessed = new AtomicLong(0);
    private final EventDispatcher eventDispatcher = new PollingEventDispatcher();

    public static void main(String[] args) throws InterruptedException {
        EventDispatcherThroughputTester tester = new EventDispatcherThroughputTester();
        int durationSeconds = 1;
        tester.measureThroughput(durationSeconds);
    }

    public void measureThroughput(int durationSeconds) throws InterruptedException {
        eventDispatcher.start();
        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
        long startTime = System.currentTimeMillis();
        long endTime = startTime + durationSeconds * 1000L;
        GlobalEventChannel.INSTANCE.subscribe(TestNamedEvent.class, event -> {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return ListenerStatus.CONTINUE;
        });
        while (System.currentTimeMillis() < endTime) {
            executor.execute(() -> {
                long eventStartTime = System.nanoTime();

                eventDispatcher.dispatch(new TestNamedEvent("this", "test"));

                long eventEndTime = System.nanoTime();
                totalExecutionTime.addAndGet(eventEndTime - eventStartTime);
                eventsProcessed.incrementAndGet();
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        long totalEvents = eventsProcessed.get();
        double throughput = totalEvents / (double) durationSeconds;
        System.out.printf("Throughput: %.2f events/second%n", throughput);

        if (totalEvents > 0) {
            double averageExecutionTime = totalExecutionTime.get() / (double) totalEvents / 1_000_000;
            System.out.printf("Average execution time per event: %f ms%n", averageExecutionTime);
        }
    }
}

package com.erzbir.albaz.dispatch.benchmark;

import com.erzbir.albaz.dispatch.EventDispatcher;
import com.erzbir.albaz.dispatch.channel.EventChannel;
import com.erzbir.albaz.dispatch.event.Event;
import com.erzbir.albaz.dispatch.internal.NotificationEventDispatcher;
import com.erzbir.albaz.dispatch.listener.ListenerStatus;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

public class EventDispatcherThroughputTester {

    private static final int TEST_DURATION_SECONDS = 10;
    private static final int THREAD_COUNT = 12;
    private static final int EVENTS_PER_SECOND_LIMIT = 1000;
    private final LongAdder eventsProcessed = new LongAdder();
    private final AtomicLong totalExecutionTimeNanos = new AtomicLong(0);
    private final EventDispatcher eventDispatcher = new NotificationEventDispatcher();

    public static void main(String[] args) throws InterruptedException {
        EventDispatcherThroughputTester tester = new EventDispatcherThroughputTester();
        tester.measureThroughput(TEST_DURATION_SECONDS);
    }

    public void measureThroughput(int durationSeconds) throws InterruptedException {
        eventDispatcher.start();
        EventChannel<Event> eventChannel = eventDispatcher.getEventChannel();

        eventChannel.subscribe(TestNamedEvent.class, event -> ListenerStatus.CONTINUE);

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        long startTime = System.currentTimeMillis();
        long endTime = startTime + durationSeconds * 1000L;

        final int sleepTimeMs = THREAD_COUNT * 1000 / EVENTS_PER_SECOND_LIMIT;

        for (int t = 0; t < THREAD_COUNT; t++) {
            final int threadId = t;
            executor.execute(() -> {
                try {
                    while (System.currentTimeMillis() < endTime) {
                        long eventStartTime = System.nanoTime();

                        eventDispatcher.dispatch(new TestNamedEvent("test", "event-" + threadId));

                        long eventEndTime = System.nanoTime();
                        totalExecutionTimeNanos.addAndGet(eventEndTime - eventStartTime);
                        eventsProcessed.increment();

                        Thread.sleep(sleepTimeMs);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        Thread.sleep(durationSeconds * 1000L + 500);

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        long totalEvents = eventsProcessed.longValue();
        long actualDurationMs = System.currentTimeMillis() - startTime;
        double throughput = totalEvents / (actualDurationMs / 1000.0);

        System.out.println("=== 测试结果 ===");
        System.out.printf("总事件数: %,d%n", totalEvents);
        System.out.printf("实际测试时间: %.2f 秒%n", actualDurationMs / 1000.0);
        System.out.printf("吞吐量: %.2f 事件/秒%n", throughput);

        if (totalEvents > 0) {
            double averageExecutionTime = totalExecutionTimeNanos.get() / (double) totalEvents / 1_000;
            System.out.printf("平均执行时间: %.3f 微秒/事件%n", averageExecutionTime);
        }
    }
}
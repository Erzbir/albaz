package com.erzbir.albaz.dispatch.internal;

import com.erzbir.albaz.dispatch.EventDispatcher;
import com.erzbir.albaz.dispatch.channel.EventChannel;
import com.erzbir.albaz.dispatch.event.CancelableEvent;
import com.erzbir.albaz.dispatch.event.Event;
import com.erzbir.albaz.logging.Log;
import com.erzbir.albaz.logging.LogFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <p>
 * 基于轮询的 {@link EventDispatcher}, 会启动一个虚拟线程轮询拉取事件
 * </p>
 * <p>
 * 会根据 {@link Event#getPriority()} 以及 {@link Event#timestamp()} 将 {@link Event} 放入内部的 {@link #eventQueue}
 * </p>
 *
 * @author Erzbir
 * @see EventDispatcher
 * @see AbstractEventDispatcher
 * @since 1.0.0
 */
public final class PollingEventDispatcher extends AbstractEventDispatcher implements EventDispatcher {
    private final Log log = LogFactory.getLog(PollingEventDispatcher.class);
    private final int batchSize = 5;
    // 同步块的锁, 用于控制线程
    private final Object dispatchLock = new Object();
    // 事件缓存队列
    private final PriorityBlockingQueue<Event> eventQueue = new PriorityBlockingQueue<>(10, new EventComparator());
    // 暂停线程标志位
    private final AtomicBoolean suspended = new AtomicBoolean(false);
    private volatile Thread dispatcherThread;
    /**
     * 守卫线程, 用于调用 {@link #await()} 后分发线程进入等待, 并且保证主线程不退出, 在调用 {@link #cancel()} 后退出
     */
    private volatile Thread guardThread;

    @Override
    protected <E extends Event> void dispatchTo(E event, EventChannel<E> channel) {
        eventQueue.add(event);
        // 如果有事件要分发, 则恢复线程
        if (suspended.get()) {
            resume();
        }
    }

    @Override
    public void start() {
        if (isActive()) {
            return;
        }
        Runnable runnable = () -> {
            super.start();
            while (isActive() && !Thread.currentThread().isInterrupted() && dispatcherThread != null) {
                try {
                    // 如果队列为空则暂让线程等待
                    if (eventQueue.isEmpty()) {
                        if (!suspended.get()) {
                            suspend();
                        }
                        continue;
                    }
                    List<Event> events = new ArrayList<>(batchSize);
                    eventQueue.drainTo(events, batchSize);
                    EventChannelDispatcher<Event> channel = EventChannelDispatcher.INSTANCE;
                    for (Event event : events) {
                        Thread.ofVirtual()
                                .name("Dispatch-Thread-" + Thread.currentThread().threadId())
                                .start(createDispatchTask(channel, event));
                    }
                } catch (InterruptedException e) {
                    log.error("Dispatching error: " + e.getMessage());
                }
            }
        };
        dispatcherThread = Thread.ofVirtual().name("Dispatch-Thread-Main").start(runnable);
    }

    /**
     * 恢复轮询
     */
    private void resume() {
        if (!isActive()) {
            return;
        }
        synchronized (dispatchLock) {
            suspended.set(false);
            dispatchLock.notifyAll();
        }
    }

    /**
     * 暂停轮询
     */
    private void suspend() throws InterruptedException {
        if (!isActive()) {
            return;
        }
        synchronized (dispatchLock) {
            suspended.set(true);
            dispatchLock.wait();
        }
    }

    private Runnable createDispatchTask(EventChannel<Event> channel, Event event) {
        return () -> {
            try {
                if (event instanceof CancelableEvent cancelableEvent && cancelableEvent.isCanceled()) {
                    return;
                }
                if (!event.isIntercepted()) {
                    log.debug("Dispatching event: " + event + " to channel: " + channel.getClass().getSimpleName());
                    channel.broadcast(event);
                }
            } catch (Throwable e) {
                log.error("Dispatching to channel: " + channel.getClass().getSimpleName() + " error: " + e.getMessage());
            }
        };
    }

    @Override
    public void join() {
        if (dispatcherThread == null) {
            return;
        }
        try {
            dispatcherThread.join();
        } catch (InterruptedException e) {
            log.error("Dispatching error when join thread: " + dispatcherThread);
        }
    }

    @Override
    public void join(long timeout) {
        if (dispatcherThread == null) {
            return;
        }
        try {
            dispatcherThread.join(timeout);
        } catch (InterruptedException e) {
            log.error("Dispatching error when join thread: " + dispatcherThread);
        }
    }

    @Override
    public void await() {
        guardThread = new Thread(() -> {
            try {
                suspend();
            } catch (InterruptedException e) {
                log.error("Dispatching error when await thread: " + dispatcherThread);
            }
        }, "Dispatch-Thread-Guard");
        guardThread.start();
    }

    @Override
    public void cancel() {
        if (!isActive()) {
            return;
        }
        activated.set(false);
        eventQueue.clear();
        // 这里需要唤醒等待的线程, 否则线程永远都不会结束
        resume();

        if (dispatcherThread != null) {
            dispatcherThread.interrupt();
            dispatcherThread = null;
        }

        if (guardThread != null) {
            guardThread.interrupt();
            guardThread = null;
        }
    }

    private static class EventComparator implements Comparator<Event> {
        @Override
        public int compare(Event e1, Event e2) {
            // 先按优先级排 (数字越小优先级越高)
            if (e1.getPriority() != e2.getPriority()) {
                return Integer.compare(e1.getPriority(), e2.getPriority());
            }
            // 优先级相同, 再按时间戳排 (越早越靠前)
            return Long.compare(e1.timestamp(), e2.timestamp());
        }
    }
}
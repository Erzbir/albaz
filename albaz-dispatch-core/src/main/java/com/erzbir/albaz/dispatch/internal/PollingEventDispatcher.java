package com.erzbir.albaz.dispatch.internal;

import com.erzbir.albaz.dispatch.event.CancelableEvent;
import com.erzbir.albaz.dispatch.event.Event;
import com.erzbir.albaz.dispatch.channel.EventChannel;
import com.erzbir.albaz.dispatch.EventDispatcher;
import com.erzbir.albaz.logging.Log;
import com.erzbir.albaz.logging.LogFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <p>
 * 基于轮询的 {@link EventDispatcher}, 会启动一个虚拟线程轮询拉取事件, 可以通过 {@link #join()} 方法阻塞 (不会阻塞调用线程).
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
    private Thread dispatcherThread;
    /**
     * 守卫线程, 用于调用 {@link #join()} 后分发线程进入等待, 并且保证主线程不退出, 在调用 {@link #cancel()} 后退出
     */
    private Thread guardThread;
    // 事件缓存队列
    private final PriorityBlockingQueue<Event> eventQueue = new PriorityBlockingQueue<>(10, Comparator.comparingInt(Event::getPriority));
    // 暂停线程标志位
    private final AtomicBoolean suspended = new AtomicBoolean(false);

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
        if (!activated.compareAndSet(false, true)) {
            return;
        }
        Runnable runnable = () -> {
            while (activated.get() && !Thread.currentThread().isInterrupted()) {
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
        synchronized (dispatchLock) {
            suspended.set(false);
            dispatchLock.notifyAll();
        }
    }

    /**
     * 暂停轮询
     */
    private void suspend() throws InterruptedException {
        synchronized (dispatchLock) {
            suspended.set(true);
            dispatchLock.wait();
        }
    }

    private Runnable createDispatchTask(EventChannel<Event> channel, Event event) {
        return () -> {
            try {
                if (event instanceof CancelableEvent cancelableEvent) {
                    if (cancelableEvent.isCanceled()) {
                        return;
                    }
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

    /**
     * 用于让主线程不退出
     */
    @Override
    public void join() {
        guardThread = new Thread(() -> {
            try {
                suspend();
            } catch (InterruptedException e) {
                log.error("Dispatching error when join thread: " + dispatcherThread);
            }
        }, "Dispatch-Thread-Guard");
        guardThread.start();
    }

    @Override
    public void cancel() {
        if (!activated.compareAndSet(true, false)) {
            return;
        }
        eventQueue.clear();
        // 这里需要唤醒等待的线程, 否则线程永远都不会结束
        resume();
        dispatcherThread.interrupt();
        guardThread.interrupt();
        dispatcherThread = null;
        guardThread = null;
    }
}
package com.erzbir.albaz.event;

import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * 基于轮询的 {@link EventDispatcher}, 需要调用
 *
 * @author Erzbir
 * @since 1.0.0
 */
@Slf4j
public class PollingEventDispatcher extends AbstractEventDispatcher implements EventDispatcher {
    // 同步块的锁, 用于控制线程
    private final Object dispatchLock = new Object();
    // 事件缓存队列
    private final PriorityBlockingQueue<Event> eventQueue = new PriorityBlockingQueue<>(10, Comparator.comparingInt(Event::getPriority));
    // 当前是否暂停
    private volatile boolean suspended = false;

    @Override
    protected <E extends Event> void dispatchTo(E event, EventChannel<E> channel) {
        eventQueue.add(event);
        // 如果有事件要分发, 则恢复线程
        if (suspended) {
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
                // 如果队列为空则暂让线程等待
                try {
                    if (eventQueue.isEmpty()) {
                        suspend();
                        continue;
                    }
                    Event event = eventQueue.take();
                    EventChannelDispatcher<Event> channel = EventChannelDispatcher.INSTANCE;
                    Thread.ofVirtual()
                            .name("Dispatcher-Sub-Thread")
                            .start(createTask(channel, event));
                } catch (InterruptedException e) {
                    log.error("Dispatching error: {}", e.getMessage());
                    cancel();
                    Thread.currentThread().interrupt();
                }
            }
        };
        // 主线程结束后程序不结束, 调用 cancel() 后结束
        new Thread(runnable, "Dispatcher-Thread").start();
    }

    private void resume() {
        synchronized (dispatchLock) {
            dispatchLock.notifyAll();
        }
    }

    private void suspend() throws InterruptedException {
        synchronized (dispatchLock) {
            suspended = true;
            dispatchLock.wait();
        }
    }

    private Runnable createTask(EventChannel<Event> channel, Event event) {
        return () -> {
            try {
                if (event instanceof CancelableEvent cancelableEvent) {
                    if (cancelableEvent.isCanceled()) {
                        return;
                    }
                }
                if (!event.isIntercepted()) {
                    log.debug("Dispatching event: {} to channel: {}", event, channel.getClass().getSimpleName());
                    channel.broadcast(new DefaultEventContext(event));
                }
            } catch (Throwable e) {
                log.error("Dispatching to channel: {} error: {}", channel.getClass().getSimpleName(), e.getMessage());
                Thread.currentThread().interrupt();
            }
        };
    }

    @Override
    public void cancel() {
        if (!activated.compareAndSet(true, false)) {
            return;
        }
        eventQueue.clear();
        // 这里需要唤醒等待的线程, 否则线程永远都不会结束
        resume();
    }
}

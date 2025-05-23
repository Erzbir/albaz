package com.erzbir.albaz.dispatch;


import com.erzbir.albaz.common.Closeable;

/**
 * <p>
 * 调度器接口, 提供生命周期
 * </p>
 *
 * @author Erzbir
 * @since 1.0.0
 */
public interface Dispatcher extends Closeable {
    void start();

    boolean isActive();

    /**
     * 阻塞当前调用线程
     */
    void join();

    void join(long timeout);

    /**
     * 等待任务全部结束, 不会阻塞调用线程
     */
    void await();
}
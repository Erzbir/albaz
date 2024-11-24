package com.erzbir.albaz.dispatch.listener;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <p>
 * 一次监听完成返回的结果, {@code CONTINUE} 表示继续监听, {@code STOP} 表示停止监听, {@code TRUNCATED} 表示截断
 * </p>
 *
 * <p>
 * 通常只需要用到 {@code CONTINUE} 和 {@code STOP} 两种, {@code TRUNCATED} 只发生在某些特殊情况
 * </p>
 *
 * @author Erzbir
 * @since 1.0.0
 */
public interface ListenerStatus {
    ListenerStatus CONTINUE = Status.CONTINUE;
    ListenerStatus TRUNCATED = Status.TRUNCATED;
    ListenerStatus STOP = Status.STOP;

    boolean isTruncated();

    boolean isContinue();


    enum Status implements ListenerStatus {
        CONTINUE(false, true),
        STOP(false, false),
        TRUNCATED(true, false);


        private final AtomicBoolean isContinue;
        private final AtomicBoolean isTruncated;


        Status(boolean isTruncated, boolean isContinue) {
            this.isContinue = new AtomicBoolean(isContinue);
            this.isTruncated = new AtomicBoolean(isTruncated);
        }

        @Override
        public boolean
        isTruncated() {
            return isTruncated.get();
        }

        @Override
        public boolean isContinue() {
            return isContinue.get();
        }

    }
}

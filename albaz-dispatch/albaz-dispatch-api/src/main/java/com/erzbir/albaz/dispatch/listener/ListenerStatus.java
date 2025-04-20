package com.erzbir.albaz.dispatch.listener;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <p>
 * 一次监听完成返回的结果, {@link #CONTINUE} 表示继续监听, {@link #STOP} 表示停止监听, {@link #TRUNCATED} 表示截断
 * </p>
 *
 * <p>
 * 通常只需要用到 {@link #CONTINUE} 和 {@link #STOP} 两种, {@link #TRUNCATED} 只发生在某些特殊情况 (例如被拦截或者抛错). 一个 {@link Listener} 进入 {@link #TRUNCATED} 状态后不会被移除, 也不会触发监听 (除非手动移除, 否则不会移除此状态的监听器)
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

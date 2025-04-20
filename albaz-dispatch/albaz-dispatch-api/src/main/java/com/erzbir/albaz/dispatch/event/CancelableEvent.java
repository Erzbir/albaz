package com.erzbir.albaz.dispatch.event;

import com.erzbir.albaz.common.Cancelable;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public interface CancelableEvent extends Cancelable, Event {
    boolean isCanceled();

    /**
     * 取消事件, 表示可以终止这个事件的生命周期, 后续无法恢复
     */
    void cancel();
}

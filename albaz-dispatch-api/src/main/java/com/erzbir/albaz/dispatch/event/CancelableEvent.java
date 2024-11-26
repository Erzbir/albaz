package com.erzbir.albaz.dispatch.event;

import com.erzbir.albaz.dispatch.common.Cancelable;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public interface CancelableEvent extends Cancelable {
    boolean isCanceled();

    void cancel();
}

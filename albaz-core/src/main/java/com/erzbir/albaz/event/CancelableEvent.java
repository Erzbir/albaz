package com.erzbir.albaz.event;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public interface CancelableEvent extends Cancelable {
    boolean isCanceled();

    // must be safe
    void cancel();
}

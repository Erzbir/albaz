package com.erzbir.albaz.event;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public interface Cancelable {
    void cancel();

    boolean isCanceled();
}

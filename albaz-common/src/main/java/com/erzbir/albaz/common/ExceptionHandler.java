package com.erzbir.albaz.common;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public interface ExceptionHandler<E> {
    void handle(Throwable throwable, E context);
}

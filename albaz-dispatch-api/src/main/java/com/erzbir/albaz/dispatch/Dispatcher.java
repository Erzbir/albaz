package com.erzbir.albaz.dispatch;


/**
 * @author Erzbir
 * @since 1.0.0
 */
public interface Dispatcher {
    void start();

    boolean isActive();

    void join();
}
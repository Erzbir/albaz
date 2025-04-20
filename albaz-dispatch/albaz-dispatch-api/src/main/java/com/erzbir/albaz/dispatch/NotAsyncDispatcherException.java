package com.erzbir.albaz.dispatch;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public class NotAsyncDispatcherException extends Exception {

    public NotAsyncDispatcherException() {
        super();
    }

    public NotAsyncDispatcherException(String message) {
        super(message);
    }

    public NotAsyncDispatcherException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotAsyncDispatcherException(Throwable cause) {
        super(cause);
    }

    public NotAsyncDispatcherException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

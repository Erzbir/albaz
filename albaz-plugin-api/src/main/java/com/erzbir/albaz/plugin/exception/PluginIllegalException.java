package com.erzbir.albaz.plugin.exception;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public class PluginIllegalException extends Exception {
    public PluginIllegalException() {
        super();
    }

    public PluginIllegalException(String message) {
        super(message);
    }

    public PluginIllegalException(String message, Throwable cause) {
        super(message, cause);
    }

    public PluginIllegalException(Throwable cause) {
        super(cause);
    }

    public PluginIllegalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

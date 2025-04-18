package com.erzbir.albaz.plugin.exception;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public class PluginNotSupportException extends PluginIllegalException {
    public PluginNotSupportException() {
        super();
    }

    public PluginNotSupportException(String message) {
        super(message);
    }

    public PluginNotSupportException(String message, Throwable cause) {
        super(message, cause);
    }

    public PluginNotSupportException(Throwable cause) {
        super(cause);
    }

    public PluginNotSupportException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

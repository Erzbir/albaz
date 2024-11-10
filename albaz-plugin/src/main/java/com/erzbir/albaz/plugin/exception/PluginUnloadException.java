package com.erzbir.albaz.plugin.exception;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public class PluginUnloadException extends PluginLoadException {
    public PluginUnloadException() {
        super();
    }

    public PluginUnloadException(String message) {
        super(message);
    }

    public PluginUnloadException(String message, Throwable cause) {
        super(message, cause);
    }

    public PluginUnloadException(Throwable cause) {
        super(cause);
    }

    public PluginUnloadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

package com.erzbir.albaz.plugin.exception;

/**
 * <p>
 * 加载一个非法插件时抛出
 * </p>
 *
 * @author Erzbir
 * @since 1.0.0
 */
public class PluginIllegalException extends RuntimeException {
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

package com.erzbir.albaz.plugin.exception;

/**
 * <p>
 * 插件无法加载时抛出, 比如在找不到主类或是一些预料之外的情况
 * </p>
 *
 * @author Erzbir
 * @since 1.0.0
 */
public class PluginLoadException extends RuntimeException {
    public PluginLoadException() {
        super();
    }

    public PluginLoadException(String message) {
        super(message);
    }

    public PluginLoadException(String message, Throwable cause) {
        super(message, cause);
    }

    public PluginLoadException(Throwable cause) {
        super(cause);
    }

    public PluginLoadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

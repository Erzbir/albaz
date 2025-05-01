package com.erzbir.albaz.plugin.internal;

import com.erzbir.albaz.logging.Log;
import com.erzbir.albaz.logging.LogFactory;
import com.erzbir.albaz.plugin.Plugin;
import com.erzbir.albaz.plugin.PluginDescription;
import com.erzbir.albaz.plugin.PluginHandle;
import com.erzbir.albaz.plugin.exception.PluginExceptionHandler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public class PluginWrapper implements Plugin {
    public final PluginDescription description;
    public final PluginHandle pluginHandle;
    private final Plugin delegate;
    private final AtomicBoolean enable = new AtomicBoolean(false);
    private final PluginExceptionHandler exceptionHandler;
    private final ExecutorService sandbox = Executors.newThreadPerTaskExecutor(Thread.ofVirtual().name("Sandbox-", 0).factory());


    public PluginWrapper(PluginHandle pluginHandle, PluginDescription description) {
        this(pluginHandle, description, DefaultPluginExceptionHandler.INSTANCE);
    }

    public PluginWrapper(PluginHandle pluginHandle, PluginDescription description, PluginExceptionHandler exceptionHandler) {
        this.delegate = pluginHandle.plugin();
        this.pluginHandle = pluginHandle;
        this.description = description;
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public void onEnable() {
        sandbox.execute(() -> {
            try {
                delegate.onEnable();
            } catch (Throwable throwable) {
                exceptionHandler.handle(throwable, pluginHandle);
            }
        });
    }

    @Override
    public void onDisable() {
        sandbox.execute(() -> {
            try {
                delegate.onDisable();
            } catch (Throwable throwable) {
                exceptionHandler.handle(throwable, pluginHandle);
            }
        });
    }

    @Override
    public void onLoad() {
        sandbox.execute(() -> {
            try {
                delegate.onLoad();
            } catch (Throwable throwable) {
                exceptionHandler.handle(throwable, pluginHandle);
            }
        });
    }

    @Override
    public void onUnLoad() {
        sandbox.execute(() -> {
            try {
                delegate.onUnLoad();
            } catch (Throwable throwable) {
                exceptionHandler.handle(throwable, pluginHandle);
            } finally {
                sandbox.shutdownNow();
                sandbox.close();
            }
        });
    }

    public boolean isEnable() {
        return enable.get();
    }

    public void enable() {
        if (!enable.get()) {

        }
        enable.set(true);
    }

    public void disable() {
        enable.set(false);
    }

    private static class DefaultPluginExceptionHandler implements PluginExceptionHandler {
        public static final PluginExceptionHandler INSTANCE = new DefaultPluginExceptionHandler();
        private static final Log log = LogFactory.getLog(DefaultPluginExceptionHandler.class);

        @Override
        public void handle(Throwable throwable, PluginHandle context) {
            log.error("Plugin: [{}] throw a exception: ", context, throwable);
        }
    }
}

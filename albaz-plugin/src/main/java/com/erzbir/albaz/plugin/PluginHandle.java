package com.erzbir.albaz.plugin;

import com.erzbir.albaz.logging.Log;
import com.erzbir.albaz.logging.LogFactory;
import com.erzbir.albaz.plugin.exception.PluginNotFoundException;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;

/**
 * <p>
 * 插件句柄
 * </p>
 *
 * @author Erzbir
 * @since 1.0.0
 */
public class PluginHandle {
    private final Log log = LogFactory.getLog(PluginHandle.class);
    private PluginLoader pluginLoader;
    private Plugin plugin;
    private Path file;
    private PluginManager pluginManager;

    public PluginHandle(Plugin plugin, Path file, PluginLoader pluginLoader, PluginManager pluginManager) {
        this.pluginLoader = pluginLoader;
        this.plugin = plugin;
        this.file = file;
        this.pluginManager = pluginManager;
    }

    public PluginLoader getPluginLoader() {
        return pluginLoader;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public Path getFile() {
        return file;
    }

    public PluginManager getPluginManager() {
        return pluginManager;
    }

    public void unload() throws PluginNotFoundException {
        pluginManager.unloadPlugin(plugin.getDescription().getId());
    }

    void destroy() {
        try {
            ClassLoader classLoader = pluginLoader.getClassLoader();
            ((Closeable) classLoader).close();
            classLoader = null;
        } catch (IOException e) {
            log.error("Failed to close ClassLoader", e);
        }
        file = null;
        pluginLoader = null;
        plugin = null;
        pluginManager = null;
        System.gc();
    }
}
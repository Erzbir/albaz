package com.erzbir.albaz.plugin;

import com.erzbir.albaz.plugin.exception.PluginUnloadException;

import java.io.Closeable;
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

    public void unload() throws PluginUnloadException {
        pluginManager.unloadPlugin(plugin.getDescription().getId());
    }

    void destroy() throws PluginUnloadException {
        try {
            ClassLoader classLoader = pluginLoader.getClassLoader();
            ((Closeable) classLoader).close();
            classLoader = null;
        } catch (Throwable e) {
            throw new PluginUnloadException("Failed to close ClassLoader", e);
        }
        file = null;
        pluginLoader = null;
        plugin = null;
        pluginManager = null;
        System.gc();
    }
}
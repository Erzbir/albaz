package com.erzbir.albaz.plugin;

import com.erzbir.albaz.plugin.exception.PluginUnloadException;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * <p>
 * 插件句柄
 * </p>
 *
 * @author Erzbir
 * @since 1.0.0
 */
@Slf4j
public class PluginHandle {
    private WeakReference<PluginLoader> pluginLoader;
    private WeakReference<Plugin> plugin;
    private WeakReference<File> file;

    public PluginHandle(Plugin plugin, File file, PluginLoader pluginLoader) {
        this.pluginLoader = new WeakReference<>(pluginLoader);
        this.plugin = new WeakReference<>(plugin);
        this.file = new WeakReference<>(file);
    }

    public PluginLoader getPluginLoader() {
        return pluginLoader.get();
    }

    public Plugin getPlugin() {
        return plugin.get();
    }

    public File getFile() {
        return file.get();
    }

    public void destroy() throws PluginUnloadException {
        try {
            pluginLoader.get().close();
        } catch (Throwable e) {
            log.error("Failed to close URLClassLoader", e);
            throw new PluginUnloadException(e);
        }
        file.enqueue();
        pluginLoader.enqueue();
        plugin.enqueue();
        file = null;
        pluginLoader = null;
        plugin = null;
        System.gc();
    }
}
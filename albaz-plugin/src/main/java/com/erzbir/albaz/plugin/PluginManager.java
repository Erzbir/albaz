package com.erzbir.albaz.plugin;

import com.erzbir.albaz.plugin.exception.PluginUnloadException;

import java.io.File;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public interface PluginManager {
    void loadPlugins();

    void loadPlugins(String pluginDir);

    void loadPlugin(File file);

    void reloadPlugins() throws PluginUnloadException;

    void reloadPlugin(String pluginId) throws PluginUnloadException;

    void enablePlugin(String pluginId);

    void enablePlugins();

    void disablePlugin(String pluginId);

    void disablePlugins();

    void unloadPlugins() throws PluginUnloadException;

    void unloadPlugin(String pluginId) throws PluginUnloadException;

    int size();
}

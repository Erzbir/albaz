package com.erzbir.albaz.plugin;

import java.io.File;
import java.util.List;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public interface PluginManager {
    void loadPlugins();

    void loadPlugins(String pluginDir);

    void loadPlugin(File file);

    void reloadPlugins();

    void reloadPlugin(String pluginId);

    void enablePlugin(String pluginId);

    void enablePlugins();

    void disablePlugin(String pluginId);

    void disablePlugins();

    void unloadPlugins();

    void unloadPlugin(String pluginId);

    int size();

    Plugin getPlugin(String pluginId);

    List<Plugin> getPlugins();

    PluginHandle getPluginHandle(String pluginId);

    List<PluginHandle> getPluginHandles();
}

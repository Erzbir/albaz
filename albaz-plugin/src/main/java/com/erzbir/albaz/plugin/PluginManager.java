package com.erzbir.albaz.plugin;

import com.erzbir.albaz.plugin.exception.PluginNotFoundException;

import java.io.File;
import java.util.List;

/**
 * TODO: class 插件加载
 *
 * @author Erzbir
 * @since 1.0.0
 */
public interface PluginManager {
    void loadPlugins();

    void loadPlugins(String pluginDir);

    void loadPlugin(File file);

    void reloadPlugins() throws PluginNotFoundException;

    void reloadPlugin(String pluginId) throws PluginNotFoundException;

    void enablePlugin(String pluginId) throws PluginNotFoundException;

    void enablePlugins() throws PluginNotFoundException;

    void disablePlugin(String pluginId) throws PluginNotFoundException;

    void disablePlugins() throws PluginNotFoundException;

    void unloadPlugins() throws PluginNotFoundException;

    void unloadPlugin(String pluginId) throws PluginNotFoundException;

    int size();

    Plugin getPlugin(String pluginId);

    List<Plugin> getPlugins();

    PluginHandle getPluginHandle(String pluginId);

    List<PluginHandle> getPluginHandles();
}

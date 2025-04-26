package com.erzbir.albaz.plugin;

import java.nio.file.Path;
import java.util.List;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public interface PluginManager {
    void loadPlugins();

    void loadPlugin(Path path);

    void enablePlugin(String pluginId);

    void enablePlugins();

    void disablePlugin(String pluginId);

    void disablePlugins();

    void unloadPlugins();

    void unloadPlugin(String pluginId);

    Plugin getPlugin(String pluginId);

    List<Plugin> getPlugins();
}

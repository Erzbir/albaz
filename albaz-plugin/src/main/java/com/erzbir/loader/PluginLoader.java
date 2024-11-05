package com.erzbir.loader;


import com.erzbir.Plugin;

import java.io.File;
import java.net.URLClassLoader;
import java.util.Set;

/**
 * @author Erzbir
 * @Date: 2023/6/16 09:52
 */
public interface PluginLoader {
    String SERVICE_PATH = "META-INF/services/com.erzbir.numeron.console.plugin.Plugin";

    void load(File file);

    URLClassLoader getClassLoader();

    void setClassLoader(URLClassLoader classLoader);

    Set<Plugin> getLoadedPlugins();

    Set<Class<?>> getClassCache();
}

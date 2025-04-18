package com.erzbir.albaz.plugin.internal.loader;

import com.erzbir.albaz.plugin.PluginLoader;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public abstract class AbstractPluginLoader implements PluginLoader {
    protected PluginClassLoader classLoader;

    public AbstractPluginLoader(ClassLoader parent) {
        classLoader = new PluginClassLoader(parent);
    }

    @Override
    public ClassLoader getClassLoader() {
        return classLoader;
    }
}

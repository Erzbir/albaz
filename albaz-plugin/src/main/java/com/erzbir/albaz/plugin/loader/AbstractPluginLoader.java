package com.erzbir.albaz.plugin.loader;

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

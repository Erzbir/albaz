package com.erzbir.albaz.plugin.internal.loader;

import com.erzbir.albaz.plugin.Plugin;
import com.erzbir.albaz.plugin.PluginLoader;
import com.erzbir.albaz.plugin.exception.PluginIllegalException;
import com.erzbir.albaz.plugin.exception.PluginNotSupportException;

import java.io.File;

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

    @Override
    public Plugin load(File file) throws PluginIllegalException {
        if (!file.isFile()) {
            throw new PluginNotSupportException(String.format("%s is not file", file.getName()));
        }
        if (!file.canRead()) {
            throw new PluginIllegalException(String.format("%s can't read", file.getName()));
        }
        return resolve(file);
    }

    protected abstract Plugin resolve(File file) throws PluginIllegalException;
}

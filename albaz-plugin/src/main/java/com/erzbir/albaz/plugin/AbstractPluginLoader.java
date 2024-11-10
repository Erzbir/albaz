package com.erzbir.albaz.plugin;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public abstract class AbstractPluginLoader extends URLClassLoader implements PluginLoader {

    public AbstractPluginLoader() {
        super(new URL[]{});
    }
}

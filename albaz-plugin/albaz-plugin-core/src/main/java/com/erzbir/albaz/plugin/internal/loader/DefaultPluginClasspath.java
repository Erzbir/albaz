package com.erzbir.albaz.plugin.internal.loader;

import com.erzbir.albaz.plugin.PluginClasspath;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public class DefaultPluginClasspath extends PluginClasspath {
    public static final String CLASSES_DIR = "bin";
    public static final String LIB_DIR = "lib";

    public DefaultPluginClasspath() {
        super();
        addClassesDirectories(CLASSES_DIR);
        addJarsDirectories(LIB_DIR);
    }

}

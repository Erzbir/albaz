package com.erzbir.albaz.plugin.internal.loader;

import com.erzbir.albaz.plugin.Plugin;
import com.erzbir.albaz.plugin.exception.PluginIllegalException;
import com.erzbir.albaz.plugin.internal.JavaPluginManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

class ClassPluginLoaderTest {
    String file = "plugins/com.test.TestClassPlugin.class";

    @Test
    void loadPlugin() throws PluginIllegalException {
        ClassPluginLoader loader = new ClassPluginLoader(getClass().getClassLoader(), new JavaPluginManager());
        Plugin plugin = loader.loadPlugin(Path.of(file));
        Assertions.assertNotNull(plugin);
        plugin.onLoad();
    }
}
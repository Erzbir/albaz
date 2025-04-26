package com.erzbir.albaz.plugin.internal.loader;

import com.erzbir.albaz.plugin.Plugin;
import com.erzbir.albaz.plugin.exception.PluginIllegalException;
import com.erzbir.albaz.plugin.internal.JavaPluginManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

class SpiPluginLoaderTest {
    String file = "plugins/plugin-test-1.0.0-all.jar";

    @Test
    void loadPlugin() throws PluginIllegalException {
        SpiPluginLoader loader = new SpiPluginLoader(getClass().getClassLoader(), new JavaPluginManager());
        Plugin plugin = loader.loadPlugin(Path.of(file));
        Assertions.assertNotNull(plugin);
        plugin.onLoad();
    }
}
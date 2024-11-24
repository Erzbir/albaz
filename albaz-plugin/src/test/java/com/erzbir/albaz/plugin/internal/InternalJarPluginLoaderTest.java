package com.erzbir.albaz.plugin.internal;

import com.erzbir.albaz.plugin.Plugin;
import com.erzbir.albaz.plugin.exception.PluginLoadException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

class InternalJarPluginLoaderTest {
    String file = "plugins/plugin-test-1.0.0-all.jar";

    @Test
    void load() throws PluginLoadException {
        InternalJarPluginLoader loader = new InternalJarPluginLoader();
        Plugin plugin = loader.load(new File(file));
        Assertions.assertNotNull(plugin);
        plugin.onLoad();
    }
}
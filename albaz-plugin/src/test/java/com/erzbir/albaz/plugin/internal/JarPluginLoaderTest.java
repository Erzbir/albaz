package com.erzbir.albaz.plugin.internal;

import com.erzbir.albaz.plugin.Plugin;
import com.erzbir.albaz.plugin.exception.PluginIllegalException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

class JarPluginLoaderTest {
    String file = "plugins/plugin-test-1.0.0-all.jar";

    @Test
    void load() throws PluginIllegalException {
        JarPluginLoader loader = new JarPluginLoader(getClass().getClassLoader());
        Plugin plugin = loader.load(new File(file));
        Assertions.assertNotNull(plugin);
        plugin.onLoad();
    }
}
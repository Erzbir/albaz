package com.erzbir.albaz.plugin.internal.loader;

import com.erzbir.albaz.plugin.Plugin;
import com.erzbir.albaz.plugin.exception.PluginIllegalException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

class FatJarPluginLoaderTest {
    String file = "plugins/plugin-test-1.0.0-all.jar";

    @Test
    void load() throws PluginIllegalException {
        FatJarPluginLoader loader = new FatJarPluginLoader(getClass().getClassLoader());
        Plugin plugin = loader.load(new File(file));
        Assertions.assertNotNull(plugin);
        plugin.onLoad();
    }
}
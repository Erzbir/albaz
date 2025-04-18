package com.erzbir.albaz.plugin.internal;

import com.erzbir.albaz.plugin.Plugin;
import com.erzbir.albaz.plugin.exception.PluginIllegalException;
import com.erzbir.albaz.plugin.internal.loader.SpiPluginLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

class SpiPluginLoaderTest {
    String file = "plugins/plugin-test-1.0.0-all.jar";

    @Test
    void load() throws PluginIllegalException {
        SpiPluginLoader loader = new SpiPluginLoader(getClass().getClassLoader());
        Plugin plugin = loader.load(new File(file));
        Assertions.assertNotNull(plugin);
        plugin.onLoad();
    }
}
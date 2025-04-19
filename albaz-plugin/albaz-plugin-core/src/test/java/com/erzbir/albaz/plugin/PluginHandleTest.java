package com.erzbir.albaz.plugin;

import com.erzbir.albaz.plugin.internal.JavaPluginManager;
import org.junit.jupiter.api.Test;

import java.util.List;

class PluginHandleTest {

    @Test
    void unload() {
        JavaPluginManager pluginManager = new JavaPluginManager();
        pluginManager.loadPlugins();
        List<PluginHandle> pluginHandles = pluginManager.getPluginHandles();
        for (PluginHandle pluginHandle : pluginHandles) {
            pluginHandle.unload();

        }
    }
}
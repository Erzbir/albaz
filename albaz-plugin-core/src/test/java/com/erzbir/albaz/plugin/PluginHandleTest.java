package com.erzbir.albaz.plugin;

import com.erzbir.albaz.plugin.exception.PluginNotFoundException;
import com.erzbir.albaz.plugin.internal.JavaPluginManager;
import org.junit.jupiter.api.Test;

import java.util.List;

class PluginHandleTest {

    @Test
    void unload() throws PluginNotFoundException {
        JavaPluginManager pluginManager = new JavaPluginManager();
        pluginManager.useServiceLoader(false);
        pluginManager.loadPlugins();
        List<PluginHandle> pluginHandles = pluginManager.getPluginHandles();
        for (PluginHandle pluginHandle : pluginHandles) {
            pluginHandle.unload();

        }
    }
}
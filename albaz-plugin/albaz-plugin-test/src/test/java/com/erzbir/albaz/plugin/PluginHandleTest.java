package com.erzbir.albaz.plugin;

import com.erzbir.albaz.plugin.exception.PluginNotFoundException;
import com.erzbir.albaz.plugin.spi.PluginManagerProvider;
import org.junit.jupiter.api.Test;

import java.util.List;

class PluginHandleTest {

    @Test
    void unload() throws PluginNotFoundException {
        PluginManager pluginManager = PluginManagerProvider.INSTANCE.getInstance();
        pluginManager.loadPlugins();
        List<PluginHandle> pluginHandles = pluginManager.getPluginHandles();
        for (PluginHandle pluginHandle : pluginHandles) {
            pluginHandle.unload();
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }
    }
}
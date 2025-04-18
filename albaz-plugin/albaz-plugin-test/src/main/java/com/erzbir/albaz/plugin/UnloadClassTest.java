package com.erzbir.albaz.plugin;

import com.erzbir.albaz.plugin.exception.PluginNotFoundException;
import com.erzbir.albaz.plugin.spi.PluginManagerProvider;

import java.util.List;

/**
 * 在 JUnit 中无法卸载 class
 * @author Erzbir
 * @since 1.0.0
 */
public class UnloadClassTest {
    public static void main(String[] args) throws PluginNotFoundException {
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

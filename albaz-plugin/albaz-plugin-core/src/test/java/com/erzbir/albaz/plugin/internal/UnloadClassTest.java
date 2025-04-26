package com.erzbir.albaz.plugin.internal;

import com.erzbir.albaz.plugin.PluginManager;
import com.erzbir.albaz.plugin.spi.PluginManagerProvider;

/**
 * 在 JUnit 中无法卸载 class
 *
 * @author Erzbir
 * @since 1.0.0
 */
public class UnloadClassTest {
    public static void main(String[] args) {
        PluginManager pluginManager = PluginManagerProvider.INSTANCE.getInstance();
        pluginManager.loadPlugins();
        pluginManager.unloadPlugins();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }
    }
}

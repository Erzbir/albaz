package com.erzbir.albaz.plugin;

import com.erzbir.albaz.plugin.exception.PluginUnloadException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

class PluginManagerTest {
    PluginManager newPluginManager() throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        Constructor<PluginManager> declaredConstructor = PluginManager.class.getDeclaredConstructor();
        declaredConstructor.setAccessible(true);
        return declaredConstructor.newInstance();
    }

    @Test
    void serviceLoad() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InterruptedException, PluginUnloadException {
        PluginManager pluginManager = newPluginManager();
        pluginManager.useServiceLoader(false);
        pluginManager.loadPlugins();
        pluginManager.enablePlugins();
        pluginManager.unloadPlugins();
        Thread.sleep(100);
        Assertions.assertEquals(0, pluginManager.size());
    }

    @Test
    void loadPlugins() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        PluginManager pluginManager = newPluginManager();
        pluginManager.loadPlugins();
        Assertions.assertEquals(1, pluginManager.size());
    }

    @Test
    void loadPlugin() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        PluginManager pluginManager = newPluginManager();
        pluginManager.loadPlugin(new File("plugins/plugin-test-1.0.0-all.jar"));
        Assertions.assertEquals(1, pluginManager.size());
    }

    @Test
    void enablePlugin() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        PluginManager pluginManager = newPluginManager();

        pluginManager.loadPlugins();
        pluginManager.enablePlugin("test");
        Assertions.assertEquals(1, pluginManager.size());
    }

    @Test
    void enablePlugins() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        PluginManager pluginManager = newPluginManager();

        pluginManager.loadPlugins();
        pluginManager.enablePlugins();
        Assertions.assertEquals(1, pluginManager.size());
    }

    @Test
    void disablePlugin() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        PluginManager pluginManager = newPluginManager();
        pluginManager.loadPlugins();
        pluginManager.disablePlugin("test");
        Assertions.assertEquals(1, pluginManager.size());
    }

    @Test
    void disablePlugins() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        PluginManager pluginManager = newPluginManager();
        pluginManager.loadPlugins();
        pluginManager.enablePlugins();
        pluginManager.disablePlugins();
        Assertions.assertEquals(1, pluginManager.size());
    }

    @Test
    void unloadPlugins() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InterruptedException, PluginUnloadException {
        PluginManager pluginManager = newPluginManager();
        pluginManager.loadPlugins();
        Assertions.assertEquals(1, pluginManager.size());
        pluginManager.unloadPlugins();
        Thread.sleep(100);
        Assertions.assertEquals(0, pluginManager.size());
    }

    @Test
    void unloadPlugin() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InterruptedException, PluginUnloadException {
        PluginManager pluginManager = newPluginManager();
        pluginManager.loadPlugins();
        Assertions.assertEquals(1, pluginManager.size());
        pluginManager.unloadPlugin("test");
        Thread.sleep(100);
        Assertions.assertEquals(0, pluginManager.size());
    }
}
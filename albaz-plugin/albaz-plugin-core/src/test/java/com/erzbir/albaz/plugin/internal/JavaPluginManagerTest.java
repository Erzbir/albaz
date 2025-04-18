package com.erzbir.albaz.plugin.internal;

import com.erzbir.albaz.plugin.exception.PluginNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

class JavaPluginManagerTest {
    static JavaPluginManager newPluginManager() throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        Constructor<JavaPluginManager> declaredConstructor = JavaPluginManager.class.getDeclaredConstructor();
        declaredConstructor.setAccessible(true);
        return declaredConstructor.newInstance();
    }

    public static void main(String[] args) throws InterruptedException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException, PluginNotFoundException {
        JavaPluginManager javaPluginManager = newPluginManager();
        javaPluginManager.loadPlugins();
        Assertions.assertEquals(1, javaPluginManager.size());
        Thread.sleep(2000);
        javaPluginManager.unloadPlugins();
        Thread.sleep(2000);
        Assertions.assertEquals(0, javaPluginManager.size());
    }

    @Test
    void serviceLoad() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InterruptedException, PluginNotFoundException {
        JavaPluginManager javaPluginManager = newPluginManager();
        javaPluginManager.useServiceLoader(false);
        javaPluginManager.loadPlugins();
        javaPluginManager.enablePlugins();
        javaPluginManager.unloadPlugins();
        Thread.sleep(100);
        Assertions.assertEquals(0, javaPluginManager.size());
    }

    @Test
    void loadPlugins() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        JavaPluginManager javaPluginManager = newPluginManager();
        javaPluginManager.loadPlugins();
        Assertions.assertEquals(1, javaPluginManager.size());
    }

    @Test
    void loadPlugin() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        JavaPluginManager javaPluginManager = newPluginManager();
        javaPluginManager.loadPlugin(new File("plugins/plugin-test-1.0.0-all.jar"));
        Assertions.assertEquals(1, javaPluginManager.size());
    }

    @Test
    void enablePlugin() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, PluginNotFoundException {
        JavaPluginManager javaPluginManager = newPluginManager();

        javaPluginManager.loadPlugins();
        javaPluginManager.enablePlugin("test");
        Assertions.assertEquals(1, javaPluginManager.size());
    }

    @Test
    void enablePlugins() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, PluginNotFoundException {
        JavaPluginManager javaPluginManager = newPluginManager();

        javaPluginManager.loadPlugins();
        javaPluginManager.enablePlugins();
        Assertions.assertEquals(1, javaPluginManager.size());
    }

    @Test
    void disablePlugin() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, PluginNotFoundException {
        JavaPluginManager javaPluginManager = newPluginManager();
        javaPluginManager.loadPlugins();
        javaPluginManager.disablePlugin("test");
        Assertions.assertEquals(1, javaPluginManager.size());
    }

    @Test
    void disablePlugins() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, PluginNotFoundException {
        JavaPluginManager javaPluginManager = newPluginManager();
        javaPluginManager.loadPlugins();
        javaPluginManager.enablePlugins();
        javaPluginManager.disablePlugins();
        Assertions.assertEquals(1, javaPluginManager.size());
    }

    @Test
    void unloadPlugins() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InterruptedException, PluginNotFoundException {
        JavaPluginManager javaPluginManager = newPluginManager();
        javaPluginManager.loadPlugins();
        Assertions.assertEquals(1, javaPluginManager.size());
        Thread.sleep(2000);
        javaPluginManager.unloadPlugins();
        Thread.sleep(2000);
        Assertions.assertEquals(0, javaPluginManager.size());
    }

    @Test
    void unloadPlugin() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InterruptedException, PluginNotFoundException {
        JavaPluginManager javaPluginManager = newPluginManager();
        javaPluginManager.loadPlugins();
        Assertions.assertEquals(1, javaPluginManager.size());
        javaPluginManager.unloadPlugin("test");
        Thread.sleep(100);
        Assertions.assertEquals(0, javaPluginManager.size());
    }
}
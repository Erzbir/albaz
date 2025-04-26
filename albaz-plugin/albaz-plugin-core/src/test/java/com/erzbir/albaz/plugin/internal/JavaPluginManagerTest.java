package com.erzbir.albaz.plugin.internal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

class JavaPluginManagerTest {

    @Test
    void loadPlugins() {
        JavaPluginManager javaPluginManager = new JavaPluginManager();
        javaPluginManager.loadPlugins();
    }

    @Test
    void loadPlugin() {
        JavaPluginManager javaPluginManager = new JavaPluginManager();
        javaPluginManager.loadPlugin(Path.of("plugins/plugin-test-1.0.0-all.jar"));
    }

    @Test
    void enablePlugin() {
        JavaPluginManager javaPluginManager = new JavaPluginManager();
        javaPluginManager.loadPlugins();
        javaPluginManager.enablePlugin("test");
    }

    @Test
    void enablePlugins() {
        JavaPluginManager javaPluginManager = new JavaPluginManager();
        javaPluginManager.loadPlugins();
        javaPluginManager.enablePlugins();
    }

    @Test
    void disablePlugin() {
        JavaPluginManager javaPluginManager = new JavaPluginManager();
        javaPluginManager.loadPlugins();
        javaPluginManager.disablePlugin("test");
    }

    @Test
    void disablePlugins() {
        JavaPluginManager javaPluginManager = new JavaPluginManager();
        javaPluginManager.loadPlugins();
        javaPluginManager.enablePlugins();
        javaPluginManager.disablePlugins();

    }

    @Test
    void unloadPlugins() throws InterruptedException {
        JavaPluginManager javaPluginManager = new JavaPluginManager();
        javaPluginManager.loadPlugins();
        Thread.sleep(2000);
        javaPluginManager.unloadPlugins();
        Thread.sleep(2000);
    }

    @Test
    void unloadPlugin() throws InterruptedException {
        JavaPluginManager javaPluginManager = new JavaPluginManager();
        javaPluginManager.loadPlugins();
        javaPluginManager.unloadPlugin("test");
        Thread.sleep(100);
        Assertions.assertThrows(NullPointerException.class, () -> javaPluginManager.getPlugin("test"));
    }

    @Test
    void size() {
        JavaPluginManager javaPluginManager = new JavaPluginManager();
        javaPluginManager.loadPlugins();
    }

    @Test
    void getPlugin() {
        JavaPluginManager javaPluginManager = new JavaPluginManager();
        javaPluginManager.loadPlugins();
        Assertions.assertNotNull(javaPluginManager.getPlugin("test"));
    }

    @Test
    void getPlugins() {
        JavaPluginManager javaPluginManager = new JavaPluginManager();
        javaPluginManager.loadPlugins();
        Assertions.assertFalse(javaPluginManager.getPlugins().isEmpty());
    }
}
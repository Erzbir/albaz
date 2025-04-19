package com.erzbir.albaz.plugin.internal;

import com.erzbir.albaz.plugin.Plugin;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

class JavaPluginManagerTest {

    @Test
    void loadPlugins() {
        JavaPluginManager javaPluginManager = new JavaPluginManager();
        javaPluginManager.loadPlugins();
        Assertions.assertTrue(javaPluginManager.size() > 0);
    }

    @Test
    void loadPlugin() {
        JavaPluginManager javaPluginManager = new JavaPluginManager();
        javaPluginManager.loadPlugin(new File("plugins/plugin-test-1.0.0-all.jar"));
        Assertions.assertTrue(javaPluginManager.size() > 0);
    }

    @Test
    void enablePlugin() {
        JavaPluginManager javaPluginManager = new JavaPluginManager();
        javaPluginManager.loadPlugins();
        javaPluginManager.enablePlugin("test");
        Assertions.assertTrue(javaPluginManager.getPlugin("test").isEnable());
    }

    @Test
    void enablePlugins() {
        JavaPluginManager javaPluginManager = new JavaPluginManager();
        javaPluginManager.loadPlugins();
        javaPluginManager.enablePlugins();
        Assertions.assertTrue(javaPluginManager.getPlugins().stream().map(Plugin::isEnable).reduce(true, (a1, a2) -> a1 && a2));
    }

    @Test
    void disablePlugin() {
        JavaPluginManager javaPluginManager = new JavaPluginManager();
        javaPluginManager.loadPlugins();
        javaPluginManager.disablePlugin("test");
        Assertions.assertFalse(javaPluginManager.getPlugin("test").isEnable());
    }

    @Test
    void disablePlugins() {
        JavaPluginManager javaPluginManager = new JavaPluginManager();
        javaPluginManager.loadPlugins();
        javaPluginManager.enablePlugins();
        javaPluginManager.disablePlugins();
        Assertions.assertFalse(javaPluginManager.getPlugins().stream().map(Plugin::isEnable).reduce(true, (a1, a2) -> a1 && a2));

    }

    @Test
    void unloadPlugins() throws InterruptedException {
        JavaPluginManager javaPluginManager = new JavaPluginManager();
        javaPluginManager.loadPlugins();
        Assertions.assertTrue(javaPluginManager.size() > 0);
        Thread.sleep(2000);
        javaPluginManager.unloadPlugins();
        Thread.sleep(2000);
        Assertions.assertEquals(0, javaPluginManager.size());
    }

    @Test
    void unloadPlugin() throws InterruptedException {
        JavaPluginManager javaPluginManager = new JavaPluginManager();
        javaPluginManager.loadPlugins();
        Assertions.assertTrue(javaPluginManager.size() > 0);
        javaPluginManager.unloadPlugin("test");
        Thread.sleep(100);
        Assertions.assertThrows(NullPointerException.class, () -> javaPluginManager.getPlugin("test"));
    }

    @Test
    void reloadPlugins() {
        JavaPluginManager javaPluginManager = new JavaPluginManager();
        javaPluginManager.loadPlugins();
        javaPluginManager.reloadPlugins();
        Assertions.assertFalse(javaPluginManager.getPlugins().stream().map(Plugin::isEnable).reduce(true, (a1, a2) -> a1 && a2));

    }

    @Test
    void reloadPlugin() {
        JavaPluginManager javaPluginManager = new JavaPluginManager();
        javaPluginManager.loadPlugins();
        javaPluginManager.reloadPlugin("test");
        Assertions.assertNotNull(javaPluginManager.getPlugin("test"));
    }

    @Test
    void size() {
        JavaPluginManager javaPluginManager = new JavaPluginManager();
        Assertions.assertEquals(0, javaPluginManager.size());
        javaPluginManager.loadPlugins();
        Assertions.assertTrue(javaPluginManager.size() > 0);
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

    @Test
    void getPluginHandle() {
        JavaPluginManager javaPluginManager = new JavaPluginManager();
        javaPluginManager.loadPlugins();
        Assertions.assertNotNull(javaPluginManager.getPluginHandle("test"));
    }

    @Test
    void getPluginHandles() {
        JavaPluginManager javaPluginManager = new JavaPluginManager();
        javaPluginManager.loadPlugins();
        Assertions.assertFalse(javaPluginManager.getPlugins().isEmpty());
    }
}
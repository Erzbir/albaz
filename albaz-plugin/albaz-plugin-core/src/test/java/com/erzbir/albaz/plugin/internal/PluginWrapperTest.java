package com.erzbir.albaz.plugin.internal;

import com.erzbir.albaz.plugin.JavaPlugin;
import com.erzbir.albaz.plugin.Plugin;
import com.erzbir.albaz.plugin.PluginHandle;
import com.erzbir.albaz.plugin.PluginDescription;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

class PluginWrapperTest {

    @Test
    void onEnable() {
        PluginWrapper wrapper = new PluginWrapper(new PluginHandle(new JunitTestPlugin(), PluginWrapperTest.class.getClassLoader(), Path.of("asd")), new PluginDescription("test", "1.0.0"));
        wrapper.onEnable();
    }

    @Test
    void onDisable() {
        PluginWrapper wrapper = new PluginWrapper(new PluginHandle(new JunitTestPlugin(), PluginWrapperTest.class.getClassLoader(), Path.of("asd")), new PluginDescription("test", "1.0.0"));
        wrapper.onDisable();
    }

    @Test
    void onLoad() {
        PluginWrapper wrapper = new PluginWrapper(new PluginHandle(new JunitTestPlugin(), PluginWrapperTest.class.getClassLoader(), Path.of("asd")), new PluginDescription("test", "1.0.0"));
        wrapper.onLoad();
    }


    @Test
    void onUnLoad() {
        PluginWrapper wrapper = new PluginWrapper(new PluginHandle(new JunitTestPlugin(), PluginWrapperTest.class.getClassLoader(), Path.of("asd")), new PluginDescription("test", "1.0.0"));
        wrapper.onUnLoad();
        Assertions.assertFalse(wrapper.isEnable());
    }

    @Test
    void enable() throws InterruptedException {
        PluginWrapper wrapper = new PluginWrapper(new PluginHandle(new JunitTestPlugin(), PluginWrapperTest.class.getClassLoader(), Path.of("asd")), new PluginDescription("test", "1.0.0"));
        wrapper.enable();
        Thread.sleep(100);
        Assertions.assertTrue(wrapper.isEnable());
    }

    @Test
    void disable() throws InterruptedException {
        PluginWrapper wrapper = new PluginWrapper(new PluginHandle(new JunitTestPlugin(), PluginWrapperTest.class.getClassLoader(), Path.of("asd")), new PluginDescription("test", "1.0.0"));
        wrapper.enable();
        wrapper.disable();
        Thread.sleep(100);
        Assertions.assertFalse(wrapper.isEnable());
    }

    @Test
    void isEnable() {
        PluginWrapper wrapper = new PluginWrapper(new PluginHandle(new JunitTestPlugin(), PluginWrapperTest.class.getClassLoader(), Path.of("asd")), new PluginDescription("test", "1.0.0"));
        Assertions.assertFalse(wrapper.isEnable());
    }

    @Test
    void getDescription() {
        PluginWrapper wrapper = new PluginWrapper(new PluginHandle(new JunitTestPlugin(), PluginWrapperTest.class.getClassLoader(), Path.of("asd")), new PluginDescription("test", "1.0.0"));
        Assertions.assertEquals("test", wrapper.description.id());
    }

    static class JunitTestPlugin extends JavaPlugin implements Plugin {
        public JunitTestPlugin() {
            super(new PluginDescription("test", "1.0.0"));
        }

        @Override
        public void onEnable() {
            System.out.println("onEnable");
            throw new RuntimeException("Test");
        }

        @Override
        public void onDisable() {
            System.out.println("onDisable");
            throw new RuntimeException("Test");
        }

        @Override
        public void onLoad() {
            System.out.println("onLoad");
            throw new RuntimeException("Test");
        }

        @Override
        public void onUnLoad() {
            System.out.println("onUnLoad");
            throw new RuntimeException("Test");
        }
    }
}
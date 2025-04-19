package com.erzbir.albaz.plugin.internal;

import com.erzbir.albaz.plugin.JavaPlugin;
import com.erzbir.albaz.plugin.Plugin;
import com.erzbir.albaz.plugin.PluginContext;
import com.erzbir.albaz.plugin.PluginDescription;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PluginWrapperTest {
    @Test
    void onEnable() {
        PluginWrapper wrapper = new PluginWrapper(new JunitTestPlugin(new PluginDescription("test", "1.0.0")));
        wrapper.onEnable();
    }

    @Test
    void onDisable() {
        PluginWrapper wrapper = new PluginWrapper(new JunitTestPlugin(new PluginDescription("test", "1.0.0")));
        wrapper.onDisable();
    }

    @Test
    void onLoad() {
        PluginWrapper wrapper = new PluginWrapper(new JunitTestPlugin(new PluginDescription("test", "1.0.0")));
        wrapper.onLoad();
    }

    @Test
    void onUnLoad() {
        PluginWrapper wrapper = new PluginWrapper(new JunitTestPlugin(new PluginDescription("test", "1.0.0")));
        wrapper.onUnLoad();
        Assertions.assertFalse(wrapper.isEnable());
    }

    @Test
    void enable() {
        PluginWrapper wrapper = new PluginWrapper(new JunitTestPlugin(new PluginDescription("test", "1.0.0")));
        wrapper.enable();
        Assertions.assertTrue(wrapper.isEnable());
    }

    @Test
    void disable() {
        PluginWrapper wrapper = new PluginWrapper(new JunitTestPlugin(new PluginDescription("test", "1.0.0")));
        wrapper.enable();
        wrapper.disable();
        Assertions.assertFalse(wrapper.isEnable());
    }

    @Test
    void isEnable() {
        PluginWrapper wrapper = new PluginWrapper(new JunitTestPlugin(new PluginDescription("test", "1.0.0")));
        Assertions.assertFalse(wrapper.isEnable());
    }

    @Test
    void getDescription() {
        PluginWrapper wrapper = new PluginWrapper(new JunitTestPlugin(new PluginDescription("test", "1.0.0")));
        Assertions.assertEquals("test", wrapper.getDescription().getId());
    }

    @Test
    void getPluginContext() {
        PluginWrapper wrapper = new PluginWrapper(new JunitTestPlugin(new PluginDescription("test", "1.0.0")));
        Assertions.assertTrue(wrapper.getPluginContext().isEmpty());
    }

    @Test
    void setPluginContext() {
        PluginWrapper wrapper = new PluginWrapper(new JunitTestPlugin(new PluginDescription("test", "1.0.0")));
        PluginContext pluginContext = new PluginContext();
        wrapper.setPluginContext(pluginContext);
        Assertions.assertEquals(pluginContext, wrapper.getPluginContext());
    }

    static class JunitTestPlugin extends JavaPlugin implements Plugin {
        public JunitTestPlugin(PluginDescription description) {
            super(description);
        }

        @Override
        public void onEnable() {
            System.out.println("onEnable");
        }

        @Override
        public void onDisable() {
            System.out.println("onDisable");
        }

        @Override
        public void onLoad() {
            System.out.println("onLoad");
        }

        @Override
        public void onUnLoad() {
            System.out.println("onUnLoad");
        }
    }
}
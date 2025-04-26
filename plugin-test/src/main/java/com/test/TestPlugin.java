package com.test;

import com.erzbir.albaz.plugin.JavaPlugin;
import com.erzbir.albaz.plugin.PluginDescription;

public class TestPlugin extends JavaPlugin {
    public static TestPlugin INSTANCE = new TestPlugin();

    public TestPlugin() {
        super(new PluginDescription.Builder("com.test.TestPlugin", "0.0.1")
                .author("Erzbir")
                .desc("this is a test")
                .build());
    }

    @Override
    public void onEnable() {
        System.out.printf("Plugin %s enabled\n", description.id());
        Test test = new Test();
        test.say();
    }

    @Override
    public void onDisable() {
        System.out.printf("Plugin %s disabled\n", description.id());

    }

    @Override
    public void onLoad() {
        System.out.printf("Plugin %s loaded\n", description.id());
    }

    @Override
    public void onUnLoad() {
        System.out.printf("Plugin %s unloaded\n", description.id());
    }
}
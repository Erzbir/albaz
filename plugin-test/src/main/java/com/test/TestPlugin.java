package com.test;

import com.erzbir.albaz.plugin.JavaPlugin;
import com.erzbir.albaz.plugin.PluginDescription;

public class TestPlugin extends JavaPlugin {
    public static TestPlugin INSTANCE = new TestPlugin();

    public TestPlugin() {
        super(new PluginDescription.Builder("test", "0.0.1")
                .author("Erzbir")
                .desc("this is a test")
                .build());
    }

    @Override
    public void onEnable() {
        System.out.println("Plugin enabled");
        Test test = new Test();
        test.say();
    }

    @Override
    public void onDisable() {
        System.out.println("Plugin disabled");
    }

    @Override
    public void onLoad() {
        System.out.println("Plugin loaded");
    }

    @Override
    public void onUnLoad() {
        System.out.println("Plugin unloaded");
    }
}
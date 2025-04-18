package com.test;

import com.erzbir.albaz.plugin.JavaPlugin;
import com.erzbir.albaz.plugin.PluginDescription;

public class TestClassPlugin extends JavaPlugin {
    public static TestClassPlugin INSTANCE = new TestClassPlugin();

    public TestClassPlugin() {
        super(new PluginDescription.Builder("test.class", "0.0.1")
                .author("Erzbir")
                .desc("this is a class plugin test")
                .build());
    }

    @Override
    public void onEnable() {
        System.out.println("Plugin enabled");
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
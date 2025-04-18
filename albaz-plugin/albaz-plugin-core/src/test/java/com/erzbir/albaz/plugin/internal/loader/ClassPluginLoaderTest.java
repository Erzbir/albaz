package com.erzbir.albaz.plugin.internal.loader;

import com.erzbir.albaz.plugin.Plugin;
import com.erzbir.albaz.plugin.exception.PluginIllegalException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

class ClassPluginLoaderTest {
    String file = "plugins/com.test.TestClassPlugin.class";

    @Test
    void load() throws PluginIllegalException {
        ClassPluginLoader loader = new ClassPluginLoader(getClass().getClassLoader());
        Plugin plugin = loader.load(new File(file));
        Assertions.assertNotNull(plugin);
        plugin.onLoad();
    }
}
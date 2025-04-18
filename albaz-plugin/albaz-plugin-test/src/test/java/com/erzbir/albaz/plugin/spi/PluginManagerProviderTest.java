package com.erzbir.albaz.plugin.spi;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PluginManagerProviderTest {

    @Test
    void getInstance() {
        Assertions.assertNotNull(PluginManagerProvider.INSTANCE.getInstance());
    }

    @Test
    void getOrDefault() {
        Assertions.assertNotNull(PluginManagerProvider.INSTANCE.getOrDefault("com.erzbir.albaz.plugin.internal.JavaPluginManager", () -> null));
    }

    @Test
    void available() {
        Assertions.assertFalse(PluginManagerProvider.INSTANCE.available().isEmpty());
    }

    @Test
    void testGetInstance() {
        Assertions.assertNotNull(PluginManagerProvider.INSTANCE.getInstance());
    }

    @Test
    void getInstances() {
        Assertions.assertFalse(PluginManagerProvider.INSTANCE.getInstances().isEmpty());

    }
}
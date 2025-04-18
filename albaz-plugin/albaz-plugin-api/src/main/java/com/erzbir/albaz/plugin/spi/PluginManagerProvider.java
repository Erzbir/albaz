package com.erzbir.albaz.plugin.spi;

import com.erzbir.albaz.common.KVProvider;
import com.erzbir.albaz.plugin.PluginManager;

import java.util.*;
import java.util.function.Supplier;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public class PluginManagerProvider implements KVProvider<String, PluginManager> {
    public static final PluginManagerProvider INSTANCE = new PluginManagerProvider();
    private static final Map<String, PluginManager> PLUGIN_MANAGERS = new HashMap<>();

    static {
        ServiceLoader<PluginManager> serviceLoader = ServiceLoader.load(PluginManager.class);
        for (PluginManager pluginManager : serviceLoader) {
            PLUGIN_MANAGERS.put(pluginManager.getClass().getName(), pluginManager);
        }
    }

    @Override
    public PluginManager getInstance(String key) {
        return PLUGIN_MANAGERS.get(key);
    }

    @Override
    public PluginManager getOrDefault(String key, Supplier<PluginManager> fallback) {
        return PLUGIN_MANAGERS.getOrDefault(key, fallback.get());
    }

    @Override
    public Set<String> available() {
        return PLUGIN_MANAGERS.keySet();
    }

    @Override
    public PluginManager getInstance() {
        for (PluginManager pluginManager : PLUGIN_MANAGERS.values()) {
            return pluginManager;
        }
        throw new IllegalStateException("No PluginManagers available");
    }

    @Override
    public List<PluginManager> getInstances() {
        return new ArrayList<>(PLUGIN_MANAGERS.values());
    }
}

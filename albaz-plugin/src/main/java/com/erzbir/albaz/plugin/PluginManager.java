package com.erzbir.albaz.plugin;

import com.erzbir.albaz.plugin.exception.PluginUnloadException;
import com.erzbir.albaz.plugin.internal.InternalJarPluginLoader;
import com.erzbir.albaz.plugin.internal.InternalSpiPluginLoader;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * <p>
 * 插件管理器, 支持热重载, 默认使用 Java 自身的 SPI 的来加载
 * </p>
 *
 * <p>
 * 使用插件 id {@link PluginDescription} 来区分每个插件, 如果加载相同 id 的插件, 那么前一个会被卸载进而加载新的插件
 * </p>
 *
 * @author Erzbir
 * @see PluginLoader
 * @see Plugin
 * @since 1.0.0
 */
@Slf4j
public class PluginManager {
    public static final PluginManager INSTANCE = new PluginManager();
    private final static String PLUGIN_DIR = "plugins";
    private final Map<String, WeakReference<PluginHandle>> plugins = new WeakHashMap<>();
    private boolean isServiceLoad = true;

    private PluginManager() {

    }

    public void isServiceLoad(boolean isServiceLoad) {
        this.isServiceLoad = isServiceLoad;
    }

    public void loadPlugins() {
        loadPlugins(PLUGIN_DIR);
    }

    public void loadPlugins(String pluginDir) {
        File[] jars = new File(pluginDir).listFiles((dir, name) -> name.endsWith(".jar"));
        if (jars == null) {
            log.warn("No plugins found in {}", pluginDir);
            return;
        }
        List<Thread> threads = new ArrayList<>();
        for (File jar : jars) {
            threads.add(Thread.ofVirtual().start(() -> loadPlugin(jar)));
        }
        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                log.warn("Interrupted while waiting for plugins to load", e);
            }
        });
    }


    public void loadPlugin(File file) {
        try {
            PluginLoader pluginLoader;
            if (isServiceLoad) {
                pluginLoader = new InternalSpiPluginLoader();
            } else {
                pluginLoader = new InternalJarPluginLoader();
            }
            Plugin plugin = pluginLoader.load(file);
            PluginHandle pluginHandle = new PluginHandle(plugin, file, pluginLoader);
            PluginDescription description = plugin.getDescription();
            if (plugins.containsKey(description.getId())) {
                log.warn("Plugin {} already loaded", plugin.getDescription().getId());
                unloadPlugin(description.getId());
                loadPlugin(file);
                return;
            }
            plugins.put(description.getId(), new WeakReference<>(pluginHandle));
            Thread.ofVirtual().start(plugin::onLoad);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void reloadPlugins() {
        unloadPlugins();
        loadPlugins();
    }

    public void reloadPlugin(String pluginId) {
        File pluginFile = plugins.get(pluginId).get().getFile();
        unloadPlugin(pluginId);
        loadPlugin(pluginFile);
    }

    public void enablePlugin(String pluginId) {
        plugins.get(pluginId).get().getPlugin().enable();
    }

    public void enablePlugins() {
        plugins.forEach((k, v) -> enablePlugin(k));
    }

    public void disablePlugin(String pluginId) {
        plugins.get(pluginId).get().getPlugin().disable();
    }


    public void disablePlugins() {
        plugins.forEach((k, v) -> disablePlugin(k));
    }

    public void unloadPlugins() {
        plugins.forEach((k, v) -> unloadPlugin(k));
    }

    public void unloadPlugin(String pluginId) {
        PluginHandle pluginHandle = plugins.get(pluginId).get();
        Thread.ofVirtual().start(() -> pluginHandle.getPlugin().onUnLoad());
        Thread.ofVirtual().start(() -> {
            try {
                pluginHandle.destroy();
            } catch (PluginUnloadException e) {
                log.error(e.getMessage(), e);
            }
        });
        Thread.ofVirtual().start(() -> plugins.remove(pluginId));
    }

    public List<Plugin> getPlugins() {
        return plugins.values().stream().map(weak -> weak.get().getPlugin()).toList();
    }

    public int size() {
        return plugins.size();
    }
}

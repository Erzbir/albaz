package com.erzbir.albaz.plugin;

import com.erzbir.albaz.logging.Log;
import com.erzbir.albaz.logging.LogFactory;
import com.erzbir.albaz.plugin.exception.PluginUnloadException;
import com.erzbir.albaz.plugin.internal.InternalJarPluginLoader;
import com.erzbir.albaz.plugin.internal.InternalSpiPluginLoader;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Collections;
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
public class PluginManager {
    public static final PluginManager INSTANCE = new PluginManager();
    private final static String PLUGIN_DIR = "plugins";
    private final Log log = LogFactory.getLog(PluginManager.class);
    private final Map<String, WeakReference<PluginHandle>> plugins = Collections.synchronizedMap(new WeakHashMap<>());
    private boolean isServiceLoad = true;

    private PluginManager() {

    }

    public void useServiceLoader(boolean isServiceLoad) {
        this.isServiceLoad = isServiceLoad;
    }

    public void loadPlugins() {
        loadPlugins(PLUGIN_DIR);
    }

    public void loadPlugins(String pluginDir) {
        File[] jars = new File(pluginDir).listFiles((dir, name) -> name.endsWith(".jar"));
        if (jars == null) {
            log.warn("No plugins found in " + pluginDir);
            return;
        }
        for (File jar : jars) {
            loadPlugin(jar);
        }
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
            if (plugin == null) {
                return;
            }
            PluginDescription description = plugin.getDescription();
            if (plugins.containsKey(description.getId())) {
                log.warn("Plugin " + plugin.getDescription().getId() + " already loaded");
                unloadPlugin(description.getId());
                loadPlugin(file);
                return;
            }
            PluginHandle pluginHandle = new PluginHandle(plugin, file, pluginLoader);
            plugins.put(description.getId(), new WeakReference<>(pluginHandle));
            plugin.onLoad();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void reloadPlugins() throws PluginUnloadException {
        unloadPlugins();
        loadPlugins();
    }

    public void reloadPlugin(String pluginId) throws PluginUnloadException {
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

    public void unloadPlugins() throws PluginUnloadException {
        for (String pluginId : plugins.keySet()) {
            unloadPlugin(pluginId);
        }
    }

    public void unloadPlugin(String pluginId) throws PluginUnloadException {
        PluginHandle pluginHandle = plugins.get(pluginId).get();
        pluginHandle.getPlugin().onUnLoad();
        pluginHandle.destroy();
        plugins.remove(pluginId);
    }

    public int size() {
        return plugins.size();
    }
}

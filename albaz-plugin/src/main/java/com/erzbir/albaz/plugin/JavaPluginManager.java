package com.erzbir.albaz.plugin;

import com.erzbir.albaz.logging.Log;
import com.erzbir.albaz.logging.LogFactory;
import com.erzbir.albaz.plugin.exception.PluginUnloadException;
import com.erzbir.albaz.plugin.internal.JarPluginLoader;
import com.erzbir.albaz.plugin.internal.SpiPluginLoader;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
 * @see PluginWrapper
 * @since 1.0.0
 */
public class JavaPluginManager implements PluginManager {
    private final static String PLUGIN_DIR = "plugins";
    private final Log log = LogFactory.getLog(JavaPluginManager.class);
    private final Map<String, PluginHandle> plugins = new ConcurrentHashMap<>();
    private boolean isServiceLoad = true;

    public JavaPluginManager() {

    }

    public void useServiceLoader(boolean isServiceLoad) {
        this.isServiceLoad = isServiceLoad;
    }

    @Override
    public void loadPlugins() {
        loadPlugins(PLUGIN_DIR);
    }

    @Override
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


    @Override
    public void loadPlugin(File file) {
        try {
            PluginLoader pluginLoader;
            if (isServiceLoad) {
                pluginLoader = new SpiPluginLoader(getClass().getClassLoader());
            } else {
                pluginLoader = new JarPluginLoader(getClass().getClassLoader());
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
            PluginHandle pluginHandle = new PluginHandle(new PluginWrapper(plugin), file.toPath(), pluginLoader, this);
            plugins.put(description.getId(), pluginHandle);
            plugin.onLoad();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void reloadPlugins() throws PluginUnloadException {
        unloadPlugins();
        loadPlugins();
    }

    @Override
    public void reloadPlugin(String pluginId) throws PluginUnloadException {
        File pluginFile = plugins.get(pluginId).getFile().toFile();
        unloadPlugin(pluginId);
        loadPlugin(pluginFile);
    }

    @Override
    public void enablePlugin(String pluginId) {
        plugins.get(pluginId).getPlugin().enable();
    }

    @Override
    public void enablePlugins() {
        plugins.forEach((k, v) -> enablePlugin(k));
    }

    @Override
    public void disablePlugin(String pluginId) {
        plugins.get(pluginId).getPlugin().disable();
    }

    @Override
    public void disablePlugins() {
        plugins.forEach((k, v) -> disablePlugin(k));
    }

    @Override
    public void unloadPlugins() throws PluginUnloadException {
        for (String pluginId : plugins.keySet()) {
            unloadPlugin(pluginId);
        }
    }

    @Override
    public void unloadPlugin(String pluginId) throws PluginUnloadException {
        PluginHandle pluginHandle = plugins.get(pluginId);
        pluginHandle.getPlugin().onUnLoad();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new PluginUnloadException(String.format("Plugin: %s unload failed when sleep", pluginId), e);
        }
        pluginHandle.destroy();
        plugins.remove(pluginId);
    }

    @Override
    public int size() {
        return plugins.size();
    }
}

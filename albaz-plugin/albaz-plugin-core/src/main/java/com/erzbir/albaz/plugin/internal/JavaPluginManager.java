package com.erzbir.albaz.plugin.internal;

import com.erzbir.albaz.logging.Log;
import com.erzbir.albaz.logging.LogFactory;
import com.erzbir.albaz.plugin.*;
import com.erzbir.albaz.plugin.exception.PluginIllegalException;
import com.erzbir.albaz.plugin.exception.PluginLoadException;
import com.erzbir.albaz.plugin.internal.loader.ClassPluginLoader;
import com.erzbir.albaz.plugin.internal.loader.FatJarPluginLoader;
import com.erzbir.albaz.plugin.internal.loader.SpiPluginLoader;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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

    public JavaPluginManager() {

    }

    @Override
    public void loadPlugins() {
        loadPlugins(PLUGIN_DIR);
    }

    @Override
    public void loadPlugins(String pluginDir) {
        File[] plugins = new File(pluginDir).listFiles((dir, name) -> name.endsWith(".jar") || name.endsWith(".class"));
        if (plugins == null || plugins.length == 0) {
            log.warn("No plugins found in " + pluginDir);
            return;
        }
        for (File plugin : plugins) {
            loadPlugin(plugin);
        }
    }


    @Override
    public void loadPlugin(File file) {
        PluginLoader pluginLoader;
        FileTypeDetector.FileType fileType;
        try {
            fileType = FileTypeDetector.detect(file);
        } catch (IOException e) {
            throw new PluginIllegalException(e);
        }
        switch (fileType) {
            case FileTypeDetector.FileType.JAR -> pluginLoader = new SpiPluginLoader(getClass().getClassLoader());
            case FileTypeDetector.FileType.CLASS -> pluginLoader = new ClassPluginLoader(getClass().getClassLoader());
            default -> throw new PluginIllegalException(file.getName() + " is not a valid plugin file");
        }
        Plugin plugin;
        try {
            plugin = pluginLoader.load(file);
        } catch (Throwable e) {
            pluginLoader = new FatJarPluginLoader(getClass().getClassLoader());
            plugin = pluginLoader.load(file);
        }
        if (plugin == null) {
            throw new PluginLoadException("Failed to load plugin " + file.getAbsolutePath());
        }
        PluginDescription description = plugin.getDescription();
        if (plugins.containsKey(description.getId())) {
            log.warn("Plugin " + plugin.getDescription().getId() + " already loaded, try to reload");
            unloadPlugin(description.getId());
            loadPlugin(file);
            return;
        }
        PluginHandle pluginHandle = new PluginHandle(new PluginWrapper(plugin), file.toPath(), pluginLoader, this);
        plugins.put(description.getId(), pluginHandle);
        plugin.onLoad();
    }

    @Override
    public void reloadPlugins() {
        unloadPlugins();
        loadPlugins();
    }

    @Override
    public void reloadPlugin(String pluginId) {
        PluginHandle pluginHandle = plugins.get(pluginId);
        if (pluginHandle == null) {
            log.warn("Plugin " + pluginId + " not found");
            return;
        }
        File pluginFile = pluginHandle.getFile().toFile();
        unloadPlugin(pluginId);
        loadPlugin(pluginFile);
    }

    @Override
    public void enablePlugin(String pluginId) {
        PluginHandle pluginHandle = plugins.get(pluginId);
        if (pluginHandle == null) {
            log.warn("Plugin " + pluginId + " not found");
            return;
        }
        if (pluginHandle.getPlugin().isEnable()) {
            log.warn("Plugin " + pluginId + " is already enabled");
            return;
        }
        pluginHandle.getPlugin().enable();
    }

    @Override
    public void enablePlugins() {
        for (String pluginId : plugins.keySet()) {
            enablePlugin(pluginId);
        }
    }

    @Override
    public void disablePlugin(String pluginId) {
        PluginHandle pluginHandle = plugins.get(pluginId);
        if (pluginHandle == null) {
            log.warn("Plugin " + pluginId + " not found");
            return;
        }
        if (!pluginHandle.getPlugin().isEnable()) {
            log.warn("Plugin " + pluginId + " is already disabled");
            return;
        }
        pluginHandle.getPlugin().disable();
    }

    @Override
    public void disablePlugins() {
        for (String pluginId : plugins.keySet()) {
            disablePlugin(pluginId);
        }
    }

    @Override
    public void unloadPlugins() {
        for (String pluginId : plugins.keySet()) {
            unloadPlugin(pluginId);
        }
    }

    @Override
    public void unloadPlugin(String pluginId) {
        PluginHandle pluginHandle = plugins.get(pluginId);
        if (pluginHandle == null) {
            log.warn("Plugin " + pluginId + " not found");
            return;
        }
        pluginHandle.getPlugin().onUnLoad();
        pluginHandle.destroy();
        plugins.remove(pluginId);
    }

    @Override
    public int size() {
        return plugins.size();
    }

    @Override
    public Plugin getPlugin(String pluginId) {
        return plugins.get(pluginId).getPlugin();
    }

    @Override
    public List<Plugin> getPlugins() {
        return plugins.values().stream().map(PluginHandle::getPlugin).collect(Collectors.toList());
    }

    @Override
    public PluginHandle getPluginHandle(String pluginId) {
        return plugins.get(pluginId);
    }

    @Override
    public List<PluginHandle> getPluginHandles() {
        return plugins.values().stream().toList();
    }
}

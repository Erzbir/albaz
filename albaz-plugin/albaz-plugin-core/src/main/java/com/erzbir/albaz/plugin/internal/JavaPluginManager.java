package com.erzbir.albaz.plugin.internal;

import com.erzbir.albaz.logging.Log;
import com.erzbir.albaz.logging.LogFactory;
import com.erzbir.albaz.plugin.*;
import com.erzbir.albaz.plugin.exception.PluginAlreadyLoadedException;
import com.erzbir.albaz.plugin.exception.PluginIllegalException;
import com.erzbir.albaz.plugin.exception.PluginNotFoundException;
import com.erzbir.albaz.plugin.internal.loader.ClassPluginLoader;
import com.erzbir.albaz.plugin.internal.loader.PluginDescFinder;
import com.erzbir.albaz.plugin.internal.loader.PluginLoader;
import com.erzbir.albaz.plugin.internal.loader.SpiPluginLoader;
import com.erzbir.albaz.plugin.internal.util.FileTypeDetector;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * 插件管理器, 支持热重载, 默认使用 Java 自身的 SPI 的来加载
 * </p>
 *
 * <p>
 * 使用插件 id {@link PluginDescription} 来区分每个插件
 * </p>
 *
 * @author Erzbir
 * @see PluginLoader
 * @see Plugin
 * @see PluginWrapper
 * @since 1.0.0
 */
public class JavaPluginManager implements PluginManager {
    public final static String PLUGINS_DIR_PROPERTY_NAME = "albaz.pluginsDir";
    private static final Log log = LogFactory.getLog(JavaPluginManager.class);
    protected static String PLUGIN_DIR = "plugins";

    static {
        String pluginsDir = System.getProperty(PLUGINS_DIR_PROPERTY_NAME);
        if (pluginsDir != null && !pluginsDir.isEmpty()) {
            PLUGIN_DIR = pluginsDir;
        }
    }

    protected final Map<String, PluginWrapper> plugins = new ConcurrentHashMap<>();
    protected final Map<String, ClassLoader> pluginClassLoaders = new ConcurrentHashMap<>();

    public JavaPluginManager() {
    }

    @Override
    public void loadPlugins() {
        loadPlugins(PLUGIN_DIR);
    }

    private void loadPlugins(String pluginDir) {
        File[] plugins = new File(pluginDir).listFiles((dir, name) -> name.endsWith(".jar") || name.endsWith(".class"));
        if (plugins == null || plugins.length == 0) {
            log.warn("No plugins found in [{}]", pluginDir);
            return;
        }
        for (File plugin : plugins) {
            loadPlugin(plugin.toPath().getFileName());
        }
    }

    public void loadPlugin(Path pluginPath) {
        if (!pluginPath.isAbsolute()) {
            pluginPath = Path.of(PLUGIN_DIR).resolve(pluginPath);
        }

        if (Files.notExists(pluginPath)) {
            throw new IllegalArgumentException(String.format("Specified plugin [%s] does not exist", pluginPath));
        }

        PluginLoader pluginLoader = createPluginLoader(pluginPath);

        Plugin plugin = pluginLoader.loadPlugin(pluginPath);
        if (plugin == null) {
            return;
        }

        if (!isJavaPlugin(plugin)) {
            log.warn("[{}] is not a Java plugin", pluginPath.getFileName());
            return;
        }

        PluginHandle pluginHandle = new PluginHandle(plugin, pluginLoader.getClassLoader(), pluginPath);
        PluginDescription description = findDescription(pluginHandle);

        if (plugins.containsKey(description.id())) {
            throw new PluginAlreadyLoadedException(String.format("Plugin [%s] already loaded with id [%s]", pluginPath.getFileName(), description.id()));
        }

        PluginWrapper pluginWrapper = new PluginWrapper(pluginHandle, description);
        registerPlugin(pluginWrapper, pluginLoader.getClassLoader(), pluginPath);
        log.info("Plugin: [{}] loaded", plugin);
    }

    private PluginDescription findDescription(PluginHandle pluginHandle) {
        return PluginDescFinder.find(pluginHandle);
    }

    private PluginLoader createPluginLoader(Path path) {
        FileTypeDetector.FileType fileType = FileTypeDetector.detect(path);

        return switch (fileType) {
            case FileTypeDetector.FileType.ZIP, FileTypeDetector.FileType.JAR -> new SpiPluginLoader(this);
            case FileTypeDetector.FileType.CLASS -> new ClassPluginLoader(this);
            default -> throw new PluginIllegalException("Unsupported plugin file type: " + fileType);
        };
    }


    private void registerPlugin(PluginWrapper plugin, ClassLoader classLoader, Path path) {
        String id = plugin.description.id();
        plugins.put(id, plugin);
        pluginClassLoaders.put(id, classLoader);
        plugin.onLoad();
    }

    private boolean isJavaPlugin(Plugin plugin) {
        return (plugin instanceof JavaPlugin);
    }

    @Override
    public void enablePlugin(String pluginId) {
        PluginWrapper plugin = plugins.get(pluginId);
        if (plugin == null) {
            throw new PluginNotFoundException(String.format("Plugin [%s] Not found", pluginId));
        }
        if (plugin.isEnable()) {
            log.warn("Plugin [{}] is already enabled", pluginId);
            return;
        }
        plugin.enable();
        plugin.onEnable();
        log.info("Plugin: [{}] enabled", pluginId);
    }

    @Override
    public void enablePlugins() {
        for (String pluginId : plugins.keySet()) {
            enablePlugin(pluginId);
        }
    }

    @Override
    public void disablePlugin(String pluginId) {
        PluginWrapper plugin = plugins.get(pluginId);
        if (plugin == null) {
            throw new PluginNotFoundException(String.format("Plugin [%s] Not found", pluginId));
        }
        if (!plugin.isEnable()) {
            log.warn("Plugin [{}] is already disabled", pluginId);
            return;
        }
        plugin.disable();
        plugin.onDisable();
        log.info("Plugin: [{}] disabled", pluginId);
    }

    @Override
    public void disablePlugins() {
        for (String pluginId : plugins.keySet()) {
            disablePlugin(pluginId);
        }
    }

    @Override
    public void unloadPlugins() {
        List<String> keys = new ArrayList<>(plugins.keySet());
        for (String pluginId : keys) {
            unloadPlugin(pluginId);
        }
    }

    @Override
    public void unloadPlugin(String pluginId) {
        PluginWrapper plugin = plugins.remove(pluginId);
        if (plugin == null) {
            throw new PluginNotFoundException(String.format("Plugin [%s] Not found", pluginId));
        }
        plugin.disable();
        plugin.onUnLoad();
        destroyPlugin(plugin);
        log.info("Plugin: [{}] unloaded", pluginId);
        plugin = null;
        System.gc();

    }

    @Override
    public Plugin getPlugin(String pluginId) {
        PluginWrapper plugin = plugins.get(pluginId);
        if (plugin == null) {
            throw new PluginNotFoundException(String.format("Plugin [%s] Not found", pluginId));
        }
        return plugin;
    }

    @Override
    public List<Plugin> getPlugins() {
        return new ArrayList<>(plugins.values());
    }

    private void destroyPlugin(PluginWrapper plugin) {
        try {
            if (pluginClassLoaders.get(plugin.description.id()) instanceof Closeable closeable) {
                closeable.close();
            }
        } catch (IOException e) {
            log.error("Failed to close ClassLoader", e);
        }
        pluginClassLoaders.remove(plugin.description.id());
        log.trace("ClassLoader of Plugin: [{}] is removed", plugin.description.id());
    }
}

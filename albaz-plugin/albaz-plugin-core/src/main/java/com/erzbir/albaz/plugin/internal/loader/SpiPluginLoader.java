package com.erzbir.albaz.plugin.internal.loader;

import com.erzbir.albaz.logging.Log;
import com.erzbir.albaz.logging.LogFactory;
import com.erzbir.albaz.plugin.Plugin;
import com.erzbir.albaz.plugin.PluginManager;
import com.erzbir.albaz.plugin.exception.PluginIllegalException;

import java.io.File;
import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * <p>
 * 通过 SPI 机制来加载 Jar 包插件, 不加载插件依赖中未使用的依赖, 是一种轻量级的加载模式
 * </p>
 *
 * @author Erzbir
 * @see AbstractPluginLoader
 * @see PluginLoader
 * @since 1.0.0
 */
public class SpiPluginLoader extends JarPluginLoader implements PluginLoader {
    private static final Log log = LogFactory.getLog(SpiPluginLoader.class);

    public SpiPluginLoader(ClassLoader parent, PluginManager pluginManager) {
        super(parent, pluginManager);
    }

    public SpiPluginLoader(PluginManager pluginManager) {
        super(pluginManager);
    }

    @Override
    protected Plugin getPluginInstance(File file) {
        classLoader.addFile(file);
        Plugin plugin;
        log.trace("Find plugin: [{}] from SPI", file.getName());
        Iterator<Plugin> iterator = ServiceLoader.load(Plugin.class, classLoader).iterator();
        if (!iterator.hasNext()) {
            throw new PluginIllegalException(String.format("Failed to load plugin %s from SPI", file.getName()));
        }
        plugin = iterator.next();
        return plugin;
    }
}

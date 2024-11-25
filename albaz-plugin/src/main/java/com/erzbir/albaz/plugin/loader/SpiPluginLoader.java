package com.erzbir.albaz.plugin.loader;

import com.erzbir.albaz.plugin.Plugin;
import com.erzbir.albaz.plugin.exception.PluginIllegalException;

import java.io.File;
import java.util.ServiceLoader;

/**
 * <p>
 * 通过 SPI 机制来加载, 不加载插件依赖中未使用的依赖, 是一种轻量级的加载模式
 * </p>
 *
 * @author Erzbir
 * @see AbstractPluginLoader
 * @see PluginLoader
 * @since 1.0.0
 */
public class SpiPluginLoader extends AbstractPluginLoader implements PluginLoader {
    public SpiPluginLoader(ClassLoader parent) {
        super(parent);
    }

    @Override
    public Plugin load(File file) throws PluginIllegalException {
        try {
            classLoader.addFile(file);
        } catch (Throwable e) {
            throw new PluginIllegalException(e);
        }
        return ServiceLoader.load(Plugin.class, classLoader).iterator().next();
    }
}

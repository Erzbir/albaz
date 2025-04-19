package com.erzbir.albaz.plugin.internal.loader;

import com.erzbir.albaz.plugin.Plugin;
import com.erzbir.albaz.plugin.PluginLoader;
import com.erzbir.albaz.plugin.exception.PluginIllegalException;
import com.erzbir.albaz.plugin.exception.PluginLoadException;
import com.erzbir.albaz.plugin.internal.FileTypeDetector;

import java.io.File;
import java.io.IOException;
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
public class SpiPluginLoader extends AbstractPluginLoader implements PluginLoader {
    public SpiPluginLoader(ClassLoader parent) {
        super(parent);
    }

    @Override
    protected Plugin resolve(File file) {
        FileTypeDetector.FileType detect;
        try {
            detect = FileTypeDetector.detect(file);
        } catch (IOException e) {
            throw new PluginIllegalException(e);
        }
        if (!file.getName().endsWith(".jar") || !detect.equals(FileTypeDetector.FileType.JAR)) {
            throw new PluginIllegalException("The file " + file.getAbsolutePath() + " is not a jar file");
        }
        classLoader.addFile(file);
        Plugin plugin;
        try {
            plugin = ServiceLoader.load(Plugin.class, classLoader).iterator().next();
        } catch (Throwable e) {
            throw new PluginLoadException(e);
        }
        return plugin;
    }
}

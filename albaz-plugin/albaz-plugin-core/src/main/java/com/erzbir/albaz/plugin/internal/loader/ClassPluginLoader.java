package com.erzbir.albaz.plugin.internal.loader;

import com.erzbir.albaz.logging.Log;
import com.erzbir.albaz.logging.LogFactory;
import com.erzbir.albaz.plugin.Plugin;
import com.erzbir.albaz.plugin.PluginManager;
import com.erzbir.albaz.plugin.exception.PluginIllegalException;
import com.erzbir.albaz.plugin.exception.PluginRuntimeException;
import com.erzbir.albaz.plugin.internal.util.FileTypeDetector;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;

/**
 * <p>
 * 加载 class 文件类型的 plugin
 * </p>
 *
 * @author Erzbir
 * @see AbstractPluginLoader
 * @see PluginLoader
 * @since 1.0.0
 */
public class ClassPluginLoader extends AbstractPluginLoader implements PluginLoader {
    private final Log log = LogFactory.getLog(getClass());

    public ClassPluginLoader(ClassLoader parent, PluginManager pluginManager) {
        super(parent, pluginManager);
    }

    public ClassPluginLoader(PluginManager pluginManager) {
        super(pluginManager);
    }

    @Override
    protected boolean isApplicable(Path pluginPath) {
        if (!FileTypeDetector.isClassFile(pluginPath)) {
            log.error("The file: [{}] is not a class file", pluginPath.toAbsolutePath());
            return false;
        }
        return true;
    }

    @Override
    protected Plugin getPluginInstance(File file) {
        log.trace("Find plugin: [{}] from class", file.getName());
        String pluginClassName = getPluginClassName(file);
        loadPluginClass(file, pluginClassName);
        Class<?> pluginClass = getPluginClass(pluginClassName);
        return getInstance(pluginClass);
    }

    private void loadPluginClass(File file, String className) {
        FileInputStream fileInputStream;
        byte[] bytes;
        try {
            fileInputStream = new FileInputStream(file);
            bytes = fileInputStream.readAllBytes();
            fileInputStream.close();
        } catch (IOException e) {
            throw new PluginRuntimeException(e);
        }
        classLoader.defineClass(null, bytes);
    }

    private Class<?> getPluginClass(String className) {
        try {
            return Class.forName(className, false, classLoader);
        } catch (ClassNotFoundException e) {
            throw new PluginIllegalException(String.format("Failed to find class: %s", e));
        }
    }

    private String getPluginClassName(File file) {
        String name = file.getName();
        return name.substring(0, name.lastIndexOf('.'));
    }
}

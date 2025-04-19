package com.erzbir.albaz.plugin.internal.loader;

import com.erzbir.albaz.plugin.Plugin;
import com.erzbir.albaz.plugin.PluginLoader;
import com.erzbir.albaz.plugin.exception.PluginIllegalException;
import com.erzbir.albaz.plugin.exception.PluginLoadException;
import com.erzbir.albaz.plugin.internal.FileTypeDetector;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;

/**
 * <p>
 * 加载 class 文件类型的 plugin
 * </p>
 *
 * @see AbstractPluginLoader
 * @see PluginLoader
 * @since 1.0.0
 */
public class ClassPluginLoader extends AbstractPluginLoader implements PluginLoader {
    public ClassPluginLoader(ClassLoader parent) {
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
        if (!file.getName().endsWith(".class") || !detect.equals(FileTypeDetector.FileType.CLASS)) {
            throw new PluginIllegalException("The file " + file.getAbsolutePath() + " is not a class file");
        }
        FileInputStream fileInputStream;
        byte[] bytes;

        try {
            fileInputStream = new FileInputStream(file);
            bytes = fileInputStream.readAllBytes();
            fileInputStream.close();
        } catch (IOException e) {
            throw new PluginLoadException(e);
        }
        String name = file.getName();
        String className = name.substring(0, name.lastIndexOf('.'));
        Class<?> pluginClass = classLoader.defineClass(className, bytes);
        Field instanceField;
        Object instance;
        try {
            instanceField = pluginClass.getDeclaredField("INSTANCE");
            instanceField.setAccessible(true);
            instance = instanceField.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new PluginLoadException("Failed to find INSTANCE in plugin: " + file.getAbsolutePath(), e);
        }
        return (Plugin) instance;
    }
}

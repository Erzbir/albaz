package com.erzbir.albaz.plugin.internal.loader;

import com.erzbir.albaz.plugin.Plugin;
import com.erzbir.albaz.plugin.PluginLoader;
import com.erzbir.albaz.plugin.exception.PluginIllegalException;
import com.erzbir.albaz.plugin.exception.PluginNotSupportException;
import com.erzbir.albaz.plugin.internal.FileTypeDetector;

import java.io.File;
import java.io.FileInputStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
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
    protected Plugin resolve(File file) throws PluginIllegalException {
        try {
            FileTypeDetector.FileType detect = FileTypeDetector.detect(file);
            if (!file.getName().endsWith(".class") || !detect.equals(FileTypeDetector.FileType.CLASS)) {
                throw new PluginNotSupportException("The file " + file.getAbsolutePath() + " is not a class file");
            }
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] bytes = fileInputStream.readAllBytes();
            fileInputStream.close();
            String name = file.getName();
            String className = name.substring(0, name.lastIndexOf('.'));
            Class<?> pluginClass = classLoader.defineClass(className, bytes);
            Field instanceField = pluginClass.getDeclaredField("INSTANCE");
            instanceField.setAccessible(true);
            Object instance = instanceField.get(null);
            return (Plugin) instance;
        } catch (Throwable e) {
            throw new PluginIllegalException(e);
        }
    }
}

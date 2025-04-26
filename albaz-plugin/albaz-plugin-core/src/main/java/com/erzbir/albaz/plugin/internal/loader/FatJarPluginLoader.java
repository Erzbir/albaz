package com.erzbir.albaz.plugin.internal.loader;

import com.erzbir.albaz.logging.Log;
import com.erzbir.albaz.logging.LogFactory;
import com.erzbir.albaz.plugin.Plugin;
import com.erzbir.albaz.plugin.PluginManager;
import com.erzbir.albaz.plugin.exception.PluginIllegalException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.jar.JarFile;

/**
 * <p>
 * 全量加载 Jar 包插件, 将其中所有依赖都加载进来
 * </p>
 *
 * @author Erzbir
 * @see AbstractPluginLoader
 * @see PluginLoader
 * @since 1.0.0
 */
public class FatJarPluginLoader extends JarPluginLoader implements PluginLoader {
    protected final static String SERVICE_PATH = "META-INF/services/com.erzbir.albaz.plugin.Plugin";
    private static final Log log = LogFactory.getLog(FatJarPluginLoader.class);

    public FatJarPluginLoader(ClassLoader parent, PluginManager pluginManager) {
        super(parent, pluginManager);
    }

    public FatJarPluginLoader(PluginManager pluginManager) {
        super(pluginManager);
    }

    @Override
    protected Plugin getPluginInstance(File file) {
        log.trace("Find plugin: [{}] from jar", file.getName());
        loadPluginClass(file);
        String pluginClassName = getPluginClassName(file);
        Class<?> pluginClass = getPluginClass(pluginClassName);
        return getInstance(pluginClass);
    }

    private void loadPluginClass(File file) {
        classLoader.addFile(file);
    }

    private Class<?> getPluginClass(String className) {
        try {
            return Class.forName(className, false, classLoader);
        } catch (ClassNotFoundException e) {
            throw new PluginIllegalException(String.format("Failed to find class: %s", e));
        }
    }

    private String getPluginClassName(File file) {
        String className;
        try (JarFile jarFile = new JarFile(file); BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(jarFile.getInputStream(jarFile.getEntry(SERVICE_PATH))))) {
            className = bufferedReader.readLine();
            log.trace("Read plugin main class: [{}]", className);
        } catch (IOException e) {
            throw new PluginIllegalException("Failed to read service file", e);
        }
        return className;
    }
}

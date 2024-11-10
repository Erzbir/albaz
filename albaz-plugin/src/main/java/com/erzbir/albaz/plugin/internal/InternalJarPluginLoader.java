package com.erzbir.albaz.plugin.internal;

import com.erzbir.albaz.plugin.AbstractPluginLoader;
import com.erzbir.albaz.plugin.Plugin;
import com.erzbir.albaz.plugin.PluginLoader;
import com.erzbir.albaz.plugin.exception.PluginIllegalException;
import com.erzbir.albaz.plugin.exception.PluginLoadException;
import com.erzbir.albaz.plugin.exception.PluginNotFoundException;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.jar.JarFile;

/**
 * <p>
 * 通过 jar 包全量加载, 将其中所有依赖都加载进来
 * </p>
 *
 * @author Erzbir
 * @see AbstractPluginLoader
 * @see PluginLoader
 * @since 1.0.0
 */
@Slf4j
public class InternalJarPluginLoader extends AbstractPluginLoader implements PluginLoader {
    @Override
    public Plugin load(File file) throws PluginLoadException {
        if (!file.isFile() || !file.getName().endsWith(".jar") || !file.canRead()) {
            throw new PluginIllegalException("Not a jar file or can't read");
        }
        JarFile jarFile;
        try {
            jarFile = new JarFile(file);
            addURL(file.toURI().toURL());
        } catch (IOException e) {
            throw new PluginIllegalException(e);
        }
        final JarFile finalJarFile = jarFile;

        finalJarFile.entries().asIterator().forEachRemaining(jarEntry -> {
            String name = jarEntry.getName();
            if (name.endsWith(".class") && !name.startsWith("META-INF/") && !name.endsWith("module-info.class") && !name.endsWith("package-info.class")) {
                String className = name.replaceAll("/", ".")
                        .replace(".class", "");
                try {
                    loadClass(className);
                } catch (Throwable ignored) {

                }
            }
        });
        return loadPlugin(jarFile);
    }

    private Plugin loadPlugin(JarFile jarFile) throws PluginLoadException {
        try (jarFile; BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(jarFile.getInputStream(jarFile.getEntry(SERVICE_PATH))))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                Class<?> clazz = Class.forName(line, true, this);
                if (Plugin.class.isAssignableFrom(clazz)) {
                    Field instanceField = clazz.getField("INSTANCE");
                    instanceField.setAccessible(true);
                    Object instance = instanceField.get(null);
                    return (Plugin) instance;
                }
            }
        } catch (NoSuchFieldException e) {
            throw new PluginNotFoundException(e);
        } catch (ClassNotFoundException | IllegalAccessException | IOException e1) {
            throw new PluginIllegalException(e1);
        }
        return null;
    }
}

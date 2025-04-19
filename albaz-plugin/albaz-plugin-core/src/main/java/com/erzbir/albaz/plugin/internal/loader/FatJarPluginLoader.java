package com.erzbir.albaz.plugin.internal.loader;

import com.erzbir.albaz.logging.Log;
import com.erzbir.albaz.logging.LogFactory;
import com.erzbir.albaz.plugin.Plugin;
import com.erzbir.albaz.plugin.PluginLoader;
import com.erzbir.albaz.plugin.exception.PluginIllegalException;
import com.erzbir.albaz.plugin.exception.PluginLoadException;
import com.erzbir.albaz.plugin.internal.FileTypeDetector;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
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
public class FatJarPluginLoader extends AbstractPluginLoader implements PluginLoader {
    protected final static String SERVICE_PATH = "META-INF/services/com.erzbir.albaz.plugin.Plugin";
    private final Log log = LogFactory.getLog(FatJarPluginLoader.class);

    public FatJarPluginLoader(ClassLoader parent) {
        super(parent);
    }

    @Override
    protected Plugin resolve(File file) {
        JarFile jarFile;
        try {
            FileTypeDetector.FileType detect = FileTypeDetector.detect(file);
            if (!file.getName().endsWith(".jar") || !detect.equals(FileTypeDetector.FileType.JAR)) {
                throw new PluginIllegalException("The file " + file.getAbsolutePath() + " is not a jar file");
            }
            jarFile = new JarFile(file);
            classLoader.addFile(file);
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
                    classLoader.loadClass(className);
                } catch (Throwable e) {
                    log.error("Failed to load class: " + className, e);
                }
            }
        });
        return loadPlugin(jarFile);
    }

    private Plugin loadPlugin(JarFile jarFile) {
        String line;
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(jarFile.getInputStream(jarFile.getEntry(SERVICE_PATH))));
            line = bufferedReader.readLine();
        } catch (IOException e) {
            throw new PluginLoadException("Failed to read service file", e);
        }
        Class<?> clazz;
        try {
            clazz = Class.forName(line, true, classLoader);
        } catch (ClassNotFoundException e) {
            throw new PluginLoadException(String.format("Failed to find class: %s in plugin: %s", line, jarFile.getName()), e);
        }
        Object instance = null;
        if (Plugin.class.isAssignableFrom(clazz)) {
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            MethodHandle instanceGetter;
            try {
                instanceGetter = lookup.findStaticGetter(clazz, "INSTANCE", clazz);
                instance = instanceGetter.invoke();
            } catch (Throwable e) {
                throw new PluginLoadException(String.format("Failed to find INSTANCE: %s in plugin: %s", line, jarFile.getName()), e);
            }

        }
        return (Plugin) instance;
    }
}

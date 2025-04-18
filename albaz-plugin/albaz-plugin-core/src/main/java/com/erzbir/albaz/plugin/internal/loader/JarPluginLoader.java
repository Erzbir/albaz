package com.erzbir.albaz.plugin.internal.loader;

import com.erzbir.albaz.logging.Log;
import com.erzbir.albaz.logging.LogFactory;
import com.erzbir.albaz.plugin.Plugin;
import com.erzbir.albaz.plugin.PluginLoader;
import com.erzbir.albaz.plugin.exception.PluginIllegalException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.jar.JarFile;

/**
 * <p>
 * 通过 Jar 包全量加载, 将其中所有依赖都加载进来
 * </p>
 *
 * @author Erzbir
 * @see AbstractPluginLoader
 * @see PluginLoader
 * @since 1.0.0
 */
public class JarPluginLoader extends AbstractPluginLoader implements PluginLoader {
    private final Log log = LogFactory.getLog(JarPluginLoader.class);
    protected final static String SERVICE_PATH = "META-INF/services/com.erzbir.albaz.plugin.Plugin";

    public JarPluginLoader(ClassLoader parent) {
        super(parent);
    }

    @Override
    public Plugin load(File file) throws PluginIllegalException {
        if (!file.isFile() || !file.getName().endsWith(".jar") || !file.canRead()) {
            throw new PluginIllegalException(String.format("%s is not a jar file or can't read", file.getName()));
        }
        JarFile jarFile;
        try {
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
                    log.error("Failed to load class " + className, e);
                }
            }
        });
        return loadPlugin(jarFile);
    }

    private Plugin loadPlugin(JarFile jarFile) throws PluginIllegalException {
        try (jarFile; BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(jarFile.getInputStream(jarFile.getEntry(SERVICE_PATH))))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                Class<?> clazz = Class.forName(line, true, classLoader);
                if (Plugin.class.isAssignableFrom(clazz)) {
                    MethodHandles.Lookup lookup = MethodHandles.lookup();
                    MethodHandle instanceGetter = lookup.findStaticGetter(clazz, "INSTANCE", clazz);
                    Object instance = instanceGetter.invoke();
                    return (Plugin) instance;
                }
            }
        } catch (Throwable e) {
            throw new PluginIllegalException(e);
        }
        throw new PluginIllegalException(String.format("Failed to load plugin: %s. Maybe service file has no contents", jarFile.getName()));
    }
}

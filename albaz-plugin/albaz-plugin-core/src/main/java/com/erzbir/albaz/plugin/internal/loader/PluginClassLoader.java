package com.erzbir.albaz.plugin.internal.loader;

import com.erzbir.albaz.logging.Log;
import com.erzbir.albaz.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public class PluginClassLoader extends URLClassLoader {

    private static final Log log = LogFactory.getLog(PluginClassLoader.class);

    private static final String JAVA_PACKAGE_PREFIX = "java.";
    private static final String JAVAX_PACKAGE_PREFIX = "javax.";
    private static final String PLUGIN_PACKAGE_PREFIX = "com.erzbir.albaz.";

    private boolean closed;

    public PluginClassLoader() {
        super(new URL[0]);
    }

    public PluginClassLoader(ClassLoader parent) {
        super(new URL[0], parent);
    }

    @Override
    public void addURL(URL url) {
        super.addURL(url);
    }

    public void addFile(File file) {
        try {
            addURL(file.getCanonicalFile().toURI().toURL());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public Class<?> loadClass(String className) throws ClassNotFoundException {
        synchronized (getClassLoadingLock(className)) {
            if (className.startsWith(JAVA_PACKAGE_PREFIX) || className.startsWith(JAVAX_PACKAGE_PREFIX)) {
                log.trace("System class: [{}] is delegated to SystemClassLoader", className);
                return findSystemClass(className);
            }

            if (className.startsWith(PLUGIN_PACKAGE_PREFIX)) {
                log.trace("System class: [{}] is delegated to SystemClassLoader", className);
                ClassLoader parent = getParent();
                if (parent != null) {
                    return parent.loadClass(className);
                }
            }

            Class<?> loadedClass = findLoadedClass(className);
            if (loadedClass != null) {
                log.trace("Found loaded class [{}]", className);
                return loadedClass;
            }

            try {
                Class<?> c = findClass(className);

                if (c != null) {
                    log.trace("Found class [{}] in classpath", className);
                    return c;
                }
            } catch (ClassNotFoundException ignored) {

            }
        }

        throw new ClassNotFoundException(className);
    }

    public void defineClass(String className, byte[] bytes) {
        defineClass(className, bytes, 0, bytes.length);
    }

    @Override
    public void close() throws IOException {
        super.close();

        closed = true;
    }

    public boolean isClosed() {
        return closed;
    }
}

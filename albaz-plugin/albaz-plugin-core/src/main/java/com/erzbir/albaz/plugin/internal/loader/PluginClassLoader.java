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
    private static final String PLUGIN_PACKAGE_PREFIX = "com.erzbir.albaz.plugin.";

    private boolean closed;

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
                return findSystemClass(className);
            }
            if (className.startsWith(PLUGIN_PACKAGE_PREFIX)) {
                return getParent().loadClass(className);
            }

            Class<?> loadedClass = findLoadedClass(className);
            if (loadedClass != null) {
                return loadedClass;
            }

            try {
                Class<?> c = findClass(className);

                if (c != null) {
                    return c;
                }
            } catch (ClassNotFoundException e) {
                getParent().loadClass(className);
            }
        }

        throw new ClassNotFoundException(className);
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

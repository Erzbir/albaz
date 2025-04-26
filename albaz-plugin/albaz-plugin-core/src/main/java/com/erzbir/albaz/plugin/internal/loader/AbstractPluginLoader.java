package com.erzbir.albaz.plugin.internal.loader;

import com.erzbir.albaz.logging.Log;
import com.erzbir.albaz.logging.LogFactory;
import com.erzbir.albaz.plugin.Plugin;
import com.erzbir.albaz.plugin.PluginClasspath;
import com.erzbir.albaz.plugin.PluginManager;
import com.erzbir.albaz.plugin.exception.PluginIllegalException;
import com.erzbir.albaz.plugin.exception.PluginRuntimeException;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public abstract class AbstractPluginLoader implements PluginLoader {
    private static final Log log = LogFactory.getLog(AbstractPluginLoader.class);
    protected PluginClassLoader classLoader;
    protected PluginClasspath pluginClasspath;
    protected PluginManager pluginManager;

    protected AbstractPluginLoader(ClassLoader parent, PluginManager pluginManager) {
        classLoader = new PluginClassLoader(parent);
        pluginClasspath = new DefaultPluginClasspath();
        this.pluginManager = pluginManager;
    }

    protected AbstractPluginLoader(PluginManager pluginManager) {
        classLoader = new PluginClassLoader();
        pluginClasspath = new DefaultPluginClasspath();
        this.pluginManager = pluginManager;
    }

    @Override
    public ClassLoader getClassLoader() {
        return classLoader;
    }

    protected void loadClasses(Path pluginPath, PluginClassLoader pluginClassLoader) {
        log.trace("Loading classes from [{}]", pluginPath);
        for (String directory : pluginClasspath.getClassesDirectories()) {
            File file = pluginPath.resolve(directory).toFile();
            if (file.exists() && file.isDirectory()) {
                pluginClassLoader.addFile(file);
            }
        }
    }

    protected void loadJars(Path pluginPath, PluginClassLoader pluginClassLoader) {
        log.trace("Loading classes from [{}]", pluginPath);
        for (String jarsDirectory : pluginClasspath.getJarsDirectories()) {
            Path file = pluginPath.resolve(jarsDirectory);
            List<File> jars = getJars(file);
            for (File jar : jars) {
                pluginClassLoader.addFile(jar);
            }
        }
    }

    private List<File> getJars(Path folder) {
        List<File> bucket = new ArrayList<>();
        getJars(bucket, folder);
        return bucket;
    }

    private void getJars(final List<File> bucket, Path folder) {
        if (Files.isDirectory(folder)) {
            File[] jars = folder.toFile().listFiles(File::isFile);
            for (int i = 0; (jars != null) && (i < jars.length); ++i) {
                bucket.add(jars[i]);
            }
            File[] directories = folder.toFile().listFiles(File::isDirectory);
            for (int i = 0; (directories != null) && (i < directories.length); ++i) {
                File directory = directories[i];
                getJars(bucket, directory.toPath());
            }
        }
    }

    @Override
    public Plugin loadPlugin(Path pluginPath) {
        if (!isApplicable(pluginPath)) {
            throw new PluginIllegalException("The file " + pluginPath.toAbsolutePath() + " is unapplicable");
        }
        loadClasses(pluginPath, classLoader);
        loadJars(pluginPath, classLoader);
        return resolvePlugin(pluginPath.toFile());
    }

    protected abstract boolean isApplicable(Path pluginPath);

    protected Plugin resolvePlugin(File file) {
        try {
            return getPluginInstance(file);
        } catch (Throwable e) {
            throw new PluginRuntimeException(e);
        }
    }

    protected abstract Plugin getPluginInstance(File file);

    protected Plugin getInstance(Class<?> pluginClass) {
        Field instance;
        try {
            instance = pluginClass.getDeclaredField("INSTANCE");
        } catch (NoSuchFieldException e) {
            throw new PluginRuntimeException("Failed to find field 'INSTANCE' in class " + pluginClass.getName(), e);
        }
        instance.setAccessible(true);
        try {
            return (Plugin) instance.get(null);
        } catch (IllegalAccessException e) {
            throw new PluginRuntimeException("Failed to find get 'INSTANCE' in class " + pluginClass.getName(), e);
        }
    }
}

package com.erzbir.albaz.plugin.internal.loader;

import com.erzbir.albaz.logging.Log;
import com.erzbir.albaz.logging.LogFactory;
import com.erzbir.albaz.plugin.PluginManager;
import com.erzbir.albaz.plugin.internal.util.FileTypeDetector;

import java.nio.file.Path;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public abstract class JarPluginLoader extends AbstractPluginLoader {
    private static final Log log = LogFactory.getLog(JarPluginLoader.class);

    protected JarPluginLoader(ClassLoader parent, PluginManager pluginManager) {
        super(parent, pluginManager);
    }

    protected JarPluginLoader(PluginManager pluginManager) {
        super(pluginManager);
    }

    @Override
    protected boolean isApplicable(Path pluginPath) {
        if (!FileTypeDetector.isJarFile(pluginPath)) {
            log.error("The file: [{}] is not a jar file", pluginPath.toAbsolutePath());
            return false;
        }
        return true;
    }
}

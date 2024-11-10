package com.erzbir.albaz.plugin;

import com.erzbir.albaz.plugin.exception.PluginLoadException;
import com.erzbir.albaz.plugin.internal.InternalJarPluginLoader;
import com.erzbir.albaz.plugin.internal.InternalSpiPluginLoader;

import java.io.File;
import java.io.IOException;

/**
 * <p>
 * 插件加载接口, 通过 jar 文件来加载插件
 * </p>
 * <p>
 * 默认提供两种实现: {@code InternalSpiPluginLoader} 和 {@code InternalJarPluginLoader}
 * </p>
 *
 * @author Erzbir
 * @see InternalSpiPluginLoader
 * @see InternalJarPluginLoader
 * @since 1.0.0
 */
public interface PluginLoader {
    String SERVICE_PATH = "META-INF/services/com.erzbir.albaz.plugin.Plugin";

    Plugin load(File file) throws PluginLoadException;

    void close() throws IOException;
}

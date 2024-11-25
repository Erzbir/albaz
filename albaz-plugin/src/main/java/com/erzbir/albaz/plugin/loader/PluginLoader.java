package com.erzbir.albaz.plugin.loader;

import com.erzbir.albaz.plugin.Plugin;
import com.erzbir.albaz.plugin.exception.PluginIllegalException;

import java.io.File;

/**
 * <p>
 * 插件加载接口, 通过 jar 文件来加载插件
 * </p>
 * <p>
 * 默认提供两种实现: {@link SpiPluginLoader} 和 {@link JarPluginLoader}
 * </p>
 *
 * @author Erzbir
 * @see SpiPluginLoader
 * @see JarPluginLoader
 * @since 1.0.0
 */
public interface PluginLoader {
    Plugin load(File file) throws PluginIllegalException;

    ClassLoader getClassLoader();
}

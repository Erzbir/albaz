package com.erzbir.albaz.plugin.internal.loader;

import com.erzbir.albaz.plugin.Plugin;

import java.nio.file.Path;

/**
 * <p>
 * 插件加载接口, 通过文件来加载插件
 * </p>
 *
 * @since 1.0.0
 */
public interface PluginLoader {
    Plugin loadPlugin(Path pluginPath);

    ClassLoader getClassLoader();
}

package com.erzbir.albaz.plugin;

import java.io.File;

/**
 * <p>
 * 插件加载接口, 通过文件来加载插件
 * </p>
 *
 * @since 1.0.0
 */
public interface PluginLoader {
    Plugin load(File file);

    ClassLoader getClassLoader();
}

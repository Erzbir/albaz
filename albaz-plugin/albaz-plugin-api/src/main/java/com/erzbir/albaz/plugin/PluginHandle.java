package com.erzbir.albaz.plugin;

import java.nio.file.Path;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public record PluginHandle(Plugin plugin, ClassLoader classLoader, Path path) {
}

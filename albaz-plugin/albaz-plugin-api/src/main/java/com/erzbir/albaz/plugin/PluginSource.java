package com.erzbir.albaz.plugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Path;

/**
 * <p>
 * Java 插件需继承这个抽象类
 * </p>
 *
 * @author Erzbir
 * @since 1.0.0
 */
public interface PluginSource {
    URI getLocation();

    InputStream openStream() throws IOException;

    Path getTempFile() throws IOException;
}
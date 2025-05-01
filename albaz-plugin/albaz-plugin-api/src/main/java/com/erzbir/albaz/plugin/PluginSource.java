package com.erzbir.albaz.plugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Path;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public interface PluginSource {
    URI getLocation();

    InputStream openStream() throws IOException;

    Path getTempFile() throws IOException;
}
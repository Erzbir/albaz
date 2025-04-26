package com.erzbir.albaz.plugin.internal;

import com.erzbir.albaz.plugin.PluginSource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public class LocalPluginSource implements PluginSource {
    private final Path path;

    public LocalPluginSource(Path path) {
        this.path = path;
    }

    @Override
    public InputStream openStream() throws IOException {
        return Files.newInputStream(path);
    }

    @Override
    public URI getLocation() {
        return path.toUri();
    }

    @Override
    public Path getTempFile() {
        return path;
    }
}

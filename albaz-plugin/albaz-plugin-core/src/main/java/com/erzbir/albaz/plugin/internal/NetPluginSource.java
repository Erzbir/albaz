package com.erzbir.albaz.plugin.internal;

import com.erzbir.albaz.plugin.PluginSource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public class NetPluginSource implements PluginSource {
    private final URL url;
    private Path tempFile;

    public NetPluginSource(String url) throws URISyntaxException, MalformedURLException {
        this.url = new URI(url).toURL();
    }

    @Override
    public InputStream openStream() throws IOException {
        return url.openStream();
    }

    @Override
    public URI getLocation() {
        try {
            return url.toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Path getTempFile() throws IOException {
        if (tempFile == null) {
            tempFile = Files.createTempFile("plugin_", Paths.get(url.getPath()).getFileName().toString());
            try (InputStream in = openStream(); OutputStream out = Files.newOutputStream(tempFile)) {
                in.transferTo(out);
            }
        }
        return tempFile;
    }

}

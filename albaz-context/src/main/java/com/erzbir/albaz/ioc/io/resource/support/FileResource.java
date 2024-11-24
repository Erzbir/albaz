package com.erzbir.albaz.ioc.io.resource.support;


import com.erzbir.albaz.ioc.io.resource.Resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author erzbir
 * @since 1.0.0
 */
public class FileResource implements Resource {

    private final File file;
    private final String path;

    public FileResource(String path) {
        this.path = path;
        file = new File(path);
    }

    public FileResource(File file) {
        this.file = file;
        path = file.getPath();
    }


    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(file);

    }
}

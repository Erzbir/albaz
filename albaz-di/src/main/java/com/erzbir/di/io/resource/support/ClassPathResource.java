package com.erzbir.di.io.resource.support;


import com.erzbir.di.io.resource.Resource;
import com.erzbir.util.ProxyUtils;
import lombok.Data;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author erzbir
 * @since 1.0.0
 */
@Data
public class ClassPathResource implements Resource {

    private final String path;
    private ClassLoader classLoader;

    public ClassPathResource(String path) {

        this(path, null);
    }

    public ClassPathResource(String path, ClassLoader classLoader) {
//        Assert.notNull(path, "Path must not be null");
        this.path = path;
        if (classLoader == null) {
            this.classLoader = (classLoader == null) ? ProxyUtils.getDefaultClassLoader() : classLoader;
        }
    }

    @Override
    public InputStream getInputStream() throws IOException {
        InputStream is = classLoader.getResourceAsStream(path);
        if (is == null) {
            throw new FileNotFoundException(this.path + " is not be loaded,because the it does not exist");
        }
        return is;
    }
}

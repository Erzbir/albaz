package com.erzbir.albaz.ioc.io.resource.support;


import com.erzbir.albaz.ioc.io.resource.Resource;
import com.erzbir.albaz.util.Assert;
import com.erzbir.albaz.util.ProxyUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author erzbir
 * @since 1.0.0
 */
public class ClassPathResource implements Resource {

    private final String path;
    private final ClassLoader classLoader;

    public ClassPathResource(String path) {

        this(path, null);
    }

    public ClassPathResource(String path, ClassLoader classLoader) {
        Assert.notNull(path, "Path must not be null");
        this.path = path;
        this.classLoader = (classLoader == null) ? ProxyUtils.getDefaultClassLoader() : classLoader;
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

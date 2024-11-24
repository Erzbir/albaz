package com.erzbir.albaz.ioc.io.loader.support;


import com.erzbir.albaz.ioc.io.loader.ResourceLoader;
import com.erzbir.albaz.ioc.io.resource.Resource;
import com.erzbir.albaz.ioc.io.resource.support.ClassPathResource;
import com.erzbir.albaz.ioc.io.resource.support.FileResource;
import com.erzbir.albaz.ioc.io.resource.support.UrlResource;
import com.erzbir.albaz.util.Assert;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @author erzbir
 * @since 1.0.0
 */
public class DefaultResourceLoader implements ResourceLoader {
    @Override
    public Resource getResource(String location) {
        Assert.notNull(location, "Location must not be null");
        if (location.startsWith(CLASSPATH_URL_PREFIX)) {
            return new ClassPathResource(location.substring(CLASSPATH_URL_PREFIX.length()));

        } else {

            try {
                URL url = new URI(location).toURL();
                return new UrlResource(url);
            } catch (MalformedURLException | URISyntaxException e) {
                return new FileResource(location);
            }
        }
    }
}

package com.erzbir.di.io.loader.support;


import com.erzbir.di.io.loader.ResourceLoader;
import com.erzbir.di.io.resource.Resource;
import com.erzbir.di.io.resource.support.ClassPathResource;
import com.erzbir.di.io.resource.support.FileResource;
import com.erzbir.di.io.resource.support.UrlResource;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author erzbir
 * @since 1.0.0
 */
public class DefaultResourceLoader implements ResourceLoader {
    @Override
    public Resource getResource(String location) {
//        Assert.notNull(location, "Location must not be null");
        if (location.startsWith(CLASSPATH_URL_PREFIX)) {
            return new ClassPathResource(location.substring(CLASSPATH_URL_PREFIX.length()));

        } else {

            try {
                URL url = new URL(location);
                return new UrlResource(url);
            } catch (MalformedURLException e) {
                // 路径不为url 则为File方式
                return new FileResource(location);
            }
        }
    }
}

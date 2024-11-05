package com.erzbir.di.io.loader;


import com.erzbir.di.io.resource.Resource;

/**
 * @author erzbir
 * @since 1.0.0
 */
public interface ResourceLoader {

    String CLASSPATH_URL_PREFIX = "classpath:";

    Resource getResource(String location);

}

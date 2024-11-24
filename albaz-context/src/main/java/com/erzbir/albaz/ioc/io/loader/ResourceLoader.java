package com.erzbir.albaz.ioc.io.loader;


import com.erzbir.albaz.ioc.io.resource.Resource;

/**
 * @author erzbir
 * @since 1.0.0
 */
public interface ResourceLoader {

    String CLASSPATH_URL_PREFIX = "classpath:";

    Resource getResource(String location);

}

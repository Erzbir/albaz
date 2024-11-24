package com.erzbir.albaz.ioc.beans.reader;


import com.erzbir.albaz.ioc.beans.registry.BeanDefinitionRegistry;
import com.erzbir.albaz.ioc.io.loader.ResourceLoader;
import com.erzbir.albaz.ioc.io.resource.Resource;

import java.io.IOException;

/**
 * @author: @zyz
 */
public interface BeanDefinitionReader {

    BeanDefinitionRegistry getRegistry();

    ResourceLoader getResourceLoader();

    void loadBeanDefinitions(Resource resource) throws IOException;

    void loadBeanDefinitions(Resource... resources);

    void loadBeanDefinitions(String... locations);
}

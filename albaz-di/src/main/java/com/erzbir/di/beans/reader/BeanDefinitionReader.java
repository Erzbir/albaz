package com.erzbir.di.beans.reader;


import com.erzbir.di.beans.registry.BeanDefinitionRegistry;
import com.erzbir.di.io.loader.ResourceLoader;
import com.erzbir.di.io.resource.Resource;

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

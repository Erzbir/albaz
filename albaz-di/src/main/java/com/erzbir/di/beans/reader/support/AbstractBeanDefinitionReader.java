package com.erzbir.di.beans.reader.support;


import com.erzbir.di.beans.reader.BeanDefinitionReader;
import com.erzbir.di.beans.registry.BeanDefinitionRegistry;
import com.erzbir.di.io.loader.ResourceLoader;
import com.erzbir.di.io.loader.support.DefaultResourceLoader;

/**
 * @author erzbir
 * @since 1.0.0
 */
public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader {

    /**
     * BeanDefinition注册器 用于注册BeanDefinition
     */
    private final BeanDefinitionRegistry registry;

    /**
     * 资源加载器 用于从file url classPath 加载资源
     */
    private ResourceLoader resourceLoader;

    protected AbstractBeanDefinitionReader(BeanDefinitionRegistry registry) {

        this(registry, new DefaultResourceLoader());


    }

    public AbstractBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader resourceLoader) {
        this.registry = registry;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public BeanDefinitionRegistry getRegistry() {
        return registry;
    }

    @Override
    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }
}

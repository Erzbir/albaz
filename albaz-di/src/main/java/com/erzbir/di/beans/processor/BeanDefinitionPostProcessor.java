package com.erzbir.di.beans.processor;


import com.erzbir.di.beans.factory.ConfigurableListableBeanFactory;

/**
 * @author: @zyz
 */
public interface BeanDefinitionPostProcessor {


    /**
     * 在所有的 BeanDefinition 加载完成后，实例化 Bean 对象之前，提供修
     * 改 BeanDefinition 属性的机制
     *
     * @param beanFactory
     */
    void postProcessBeanDefinition(ConfigurableListableBeanFactory beanFactory);

}

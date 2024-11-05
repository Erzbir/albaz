package com.erzbir.di.beans.factory;


import com.erzbir.di.beans.po.BeanDefinition;
import com.erzbir.di.beans.processor.BeanPostProcessor;

/**
 * @author erzbir
 * @since 1.0.0
 */
public interface ConfigurableListableBeanFactory extends ListableBeanFactory, AutowireCapableBeanFactory, ConfigurableBeanFactory {


    /**
     * 获取BeanDefinition
     *
     * @param beanName
     * @return
     */
    BeanDefinition getBeanDefinition(String beanName);

    /**
     * 实例化所有单例对象
     */
    void preInstantiateSingletons();

    /**
     * 添加beanPostProcessor处理器到缓存
     *
     * @param beanPostProcessor
     */
    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);
}

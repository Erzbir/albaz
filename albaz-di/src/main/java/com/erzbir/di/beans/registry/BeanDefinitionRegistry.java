package com.erzbir.di.beans.registry;


import com.erzbir.di.beans.po.BeanDefinition;

/**
 * @author erzbir
 * @since 1.0.0
 */
public interface BeanDefinitionRegistry {

    /**
     * 添加beanDefinition到Bean容器
     *
     * @param beanName
     * @param beanDefinition
     */
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

    /**
     * 根据beanName获取BeanDefinition
     *
     * @param beanName
     * @return
     */
    BeanDefinition getBeanDefinition(String beanName);

    /**
     * 检查容器中是否含有BeanDefinition
     *
     * @param beanName
     */
    boolean containsBeanDefinition(String beanName);

    /**
     * 获取容器中所有BeanDefinitionName
     *
     * @return
     */
    String[] getBeanDefinitionNames();
}

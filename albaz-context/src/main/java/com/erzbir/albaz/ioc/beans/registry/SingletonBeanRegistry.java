package com.erzbir.albaz.ioc.beans.registry;

/**
 * @author erzbir
 * @since 1.0.0
 */
public interface SingletonBeanRegistry {
    /**
     * 获取单例对象
     *
     * @param beanName
     * @return
     */
    Object getSingleton(String beanName);

    /**
     * 注册单例对象到单例容器中
     *
     * @param beanName
     * @param singletonObject
     */
    void registerSingleton(String beanName, Object singletonObject);
}

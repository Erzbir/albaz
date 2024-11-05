package com.erzbir.di.beans.factory;

/**
 * @author erzbir
 * @since 1.0.0
 */
public interface FactoryBean<T> {

    /**
     * 获得对象
     *
     * @return
     */
    T getObject();

    /**
     * 获得对象的Clas类
     *
     * @return
     */
    Class<?> getObjectType();

    /**
     * 判断是否是单例
     *
     * @return
     */
    boolean isSingleton();
}

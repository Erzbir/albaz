package com.erzbir.albaz.ioc.beans.factory;

/**
 * @author erzbir
 * @since 1.0.0
 */
public interface BeanFactory {


    Object getBean(String name);

    Object getBean(String name, Object... args);

    <T> T getBean(String name, Class<T> requiredType);

    <T> T getBean(Class<T> requiredType);


}

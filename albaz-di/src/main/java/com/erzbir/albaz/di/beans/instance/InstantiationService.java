package com.erzbir.albaz.di.beans.instance;


import com.erzbir.albaz.di.beans.po.BeanDefinition;

import java.lang.reflect.Constructor;

/**
 * @author erzbir
 * @since 1.0.0
 */
public interface InstantiationService {
    Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor constructor, Object[] args);
}

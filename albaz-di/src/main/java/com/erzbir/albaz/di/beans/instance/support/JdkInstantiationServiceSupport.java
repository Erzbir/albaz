package com.erzbir.albaz.di.beans.instance.support;


import com.erzbir.albaz.di.beans.instance.InstantiationService;
import com.erzbir.albaz.di.beans.po.BeanDefinition;
import com.erzbir.albaz.di.exception.BeanException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author erzbir
 * @since 1.0.0
 */
public class JdkInstantiationServiceSupport implements InstantiationService {
    @Override
    public Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor constructor, Object[] args) {
        Class<?> clazz = beanDefinition.getBeanClass();

        try {
            if (constructor == null) {
                return clazz.getDeclaredConstructor().newInstance();
            } else {
                return clazz.getDeclaredConstructor(constructor.getParameterTypes()).newInstance(args);
            }
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            throw new BeanException("Failed to instantiate the class [" + clazz.getName() + "]", e);
        }


    }
}

package com.erzbir.di.beans.instance.support;//package com.erzbir.beans.instance.support;


import com.erzbir.di.beans.instance.InstantiationService;
import com.erzbir.di.beans.po.BeanDefinition;
import com.erzbir.di.exception.BeanException;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.NoOp;

import java.lang.reflect.Constructor;

/**
 * @author erzbir
 * @since 1.0.0
 */
public class CglibInstantiationServiceSupport implements InstantiationService {
    @Override
    public Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor constructor, Object[] args) {
        Class<?> clazz = beanDefinition.getBeanClass();

        try {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(clazz);
            enhancer.setCallback(new NoOp() {
                @Override
                public int hashCode() {
                    return super.hashCode();
                }
            });
            if (null == constructor) {
                return enhancer.create();
            }

            return enhancer.create(constructor.getParameterTypes(), args);
        } catch (Exception e) {
            throw new BeanException(beanName + ": " + beanDefinition.getBeanClass().getName() + "  d not be instantiated ", e);
        }

    }
}

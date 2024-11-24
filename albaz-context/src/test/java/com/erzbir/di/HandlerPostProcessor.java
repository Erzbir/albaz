package com.erzbir.di;

import com.erzbir.albaz.ioc.beans.annotation.Component;
import com.erzbir.albaz.ioc.beans.processor.BeanPostProcessor;
import com.erzbir.albaz.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * @author Erzbir
 * @since 1.0.0
 */
@Component
public class HandlerPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        Class<?> aClass = bean.getClass();
        Method[] methods = ReflectionUtils.getUniqueDeclaredMethods(aClass, method -> method.isAnnotationPresent(Handler.class));
        for (Method method : methods) {
            if (method.isAnnotationPresent(Filter.class)) {
                System.out.println("asassas");
            }
        }

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }
}

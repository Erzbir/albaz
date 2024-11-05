package com.erzbir.di.context;

import com.erzbir.di.beans.processor.BeanPostProcessor;
import com.erzbir.di.beans.processor.InstantiationAwareBeanPostProcessor;

/**
 * @author erzbir
 * @since 1.0.0
 */
public interface SmartInstantiationAwareBeanPostProcessor extends InstantiationAwareBeanPostProcessor, BeanPostProcessor {

    /**
     * 如果 bean 需要被代理，返回代理对象；不需要被代理直接返回原始对象。
     *
     * @param bean
     * @param beanName
     * @return
     * @throws RuntimeException
     */
    default Object getEarlyBeanReference(Object bean, String beanName) throws RuntimeException {
        return bean;
    }

    @Override
    default Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    @Override
    default Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }
}

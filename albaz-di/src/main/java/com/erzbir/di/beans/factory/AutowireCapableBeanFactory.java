package com.erzbir.di.beans.factory;

import com.erzbir.di.exception.BeanException;

/**
 * @author erzbir
 * @since 1.0.0
 */
public interface AutowireCapableBeanFactory {

    Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName);

    Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName);

    public Object initializeBean(Object existingBean, String beanName) throws BeanException;
}

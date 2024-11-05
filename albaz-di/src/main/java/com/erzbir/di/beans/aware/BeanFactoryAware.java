package com.erzbir.di.beans.aware;


import com.erzbir.di.beans.factory.BeanFactory;

/**
 * @author erzbir
 * @since 1.0.0
 */
public interface BeanFactoryAware extends Aware {
    void setBeanFactory(BeanFactory beanFactory);
}

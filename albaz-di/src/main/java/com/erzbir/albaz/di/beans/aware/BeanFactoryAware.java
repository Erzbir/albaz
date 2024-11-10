package com.erzbir.albaz.di.beans.aware;


import com.erzbir.albaz.di.beans.factory.BeanFactory;

/**
 * @author erzbir
 * @since 1.0.0
 */
public interface BeanFactoryAware extends Aware {
    void setBeanFactory(BeanFactory beanFactory);
}

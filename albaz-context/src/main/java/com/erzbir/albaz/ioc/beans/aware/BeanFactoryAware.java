package com.erzbir.albaz.ioc.beans.aware;


import com.erzbir.albaz.ioc.beans.factory.BeanFactory;

/**
 * @author erzbir
 * @since 1.0.0
 */
public interface BeanFactoryAware extends Aware {
    void setBeanFactory(BeanFactory beanFactory);
}

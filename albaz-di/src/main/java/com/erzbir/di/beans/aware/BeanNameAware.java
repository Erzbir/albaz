package com.erzbir.di.beans.aware;

/**
 * @author erzbir
 * @since 1.0.0
 */
public interface BeanNameAware extends Aware {

    void setBeanName(String beanName);
}

package com.erzbir.di.context;

/**
 * @author erzbir
 * @since 1.0.0
 */
public interface InitializingBean {

    /**
     * Bean属性填充后调用
     */
    void afterPropertiesSet();
}

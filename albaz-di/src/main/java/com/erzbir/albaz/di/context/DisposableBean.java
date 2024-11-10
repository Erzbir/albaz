package com.erzbir.albaz.di.context;

/**
 * @author erzbir
 * @since 1.0.0
 */
public interface DisposableBean {

    /**
     * 销毁Bean
     */
    void destroy();
}

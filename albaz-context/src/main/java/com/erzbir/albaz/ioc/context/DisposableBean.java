package com.erzbir.albaz.ioc.context;

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

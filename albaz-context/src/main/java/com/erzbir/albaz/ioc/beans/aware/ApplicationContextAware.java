package com.erzbir.albaz.ioc.beans.aware;


import com.erzbir.albaz.ioc.context.ApplicationContext;

/**
 * @author erzbir
 * @since 1.0.0
 */
public interface ApplicationContextAware extends Aware {

    void setApplicationContext(ApplicationContext applicationContext);
}

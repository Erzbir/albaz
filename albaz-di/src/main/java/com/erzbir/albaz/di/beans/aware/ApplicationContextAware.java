package com.erzbir.albaz.di.beans.aware;


import com.erzbir.albaz.di.context.ApplicationContext;

/**
 * @author erzbir
 * @since 1.0.0
 */
public interface ApplicationContextAware extends Aware {

    void setApplicationContext(ApplicationContext applicationContext);
}

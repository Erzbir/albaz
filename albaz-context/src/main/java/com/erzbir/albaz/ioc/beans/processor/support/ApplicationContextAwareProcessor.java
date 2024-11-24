package com.erzbir.albaz.ioc.beans.processor.support;


import com.erzbir.albaz.ioc.beans.aware.ApplicationContextAware;
import com.erzbir.albaz.ioc.beans.processor.BeanPostProcessor;
import com.erzbir.albaz.ioc.context.ApplicationContext;

/**
 * @author erzbir
 * @since 1.0.0
 */
public class ApplicationContextAwareProcessor implements BeanPostProcessor {


    private final ApplicationContext applicationContext;

    public ApplicationContextAwareProcessor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        if (bean instanceof ApplicationContextAware) {
            ((ApplicationContextAware) bean).setApplicationContext(applicationContext);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }

}

package com.erzbir.di.beans.processor.support;


import com.erzbir.di.beans.aware.ApplicationContextAware;
import com.erzbir.di.beans.processor.BeanPostProcessor;
import com.erzbir.di.context.ApplicationContext;

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

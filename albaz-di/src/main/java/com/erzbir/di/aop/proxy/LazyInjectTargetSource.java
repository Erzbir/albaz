package com.erzbir.di.aop.proxy;


import com.erzbir.di.context.ApplicationContext;

/**
 * @author erzbir
 * @since 1.0.0
 */
public class LazyInjectTargetSource implements TargetSource {

    private final ApplicationContext applicationContext;
    private final String beanName;

    public LazyInjectTargetSource(ApplicationContext applicationContext, String beanName) {
        this.applicationContext = applicationContext;
        this.beanName = beanName;
    }

    @Override
    public Object getTarget() {
        return applicationContext.getBean(beanName);
    }
}

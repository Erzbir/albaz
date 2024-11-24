package com.erzbir.albaz.ioc.aop;

/**
 * @author erzbir
 * @since 1.0.0
 */
public class SingletonAspectInstanceFactory implements AspectInstanceFactory {

    private Object aspectInstance;

    public SingletonAspectInstanceFactory(Object aspectInstance) {
        this.aspectInstance = aspectInstance;
    }

    @Override
    public Object getAspectInstance() {
        return this.aspectInstance;
    }
}

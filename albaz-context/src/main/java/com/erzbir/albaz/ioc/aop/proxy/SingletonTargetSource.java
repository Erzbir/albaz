package com.erzbir.albaz.ioc.aop.proxy;

/**
 * @author erzbir
 * @since 1.0.0
 */
public class SingletonTargetSource implements TargetSource {

    private final Object target;

    public SingletonTargetSource(Object target) {
        this.target = target;
    }

    @Override
    public Object getTarget() {
        return this.target;
    }
}

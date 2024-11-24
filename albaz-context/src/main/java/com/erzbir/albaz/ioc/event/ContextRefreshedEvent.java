package com.erzbir.albaz.ioc.event;

/**
 * @author erzbir
 * @since 1.0.0
 */
public class ContextRefreshedEvent extends ApplicationContextEvent {

    public ContextRefreshedEvent(Object source) {
        super(source);
    }
}

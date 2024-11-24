package com.erzbir.albaz.ioc.event;

/**
 * @author erzbir
 * @since 1.0.0
 */
public class ContextClosedEvent extends ApplicationContextEvent {

    public ContextClosedEvent(Object source) {
        super(source);
    }
}

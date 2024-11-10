package com.erzbir.albaz.di.event;


import com.erzbir.albaz.di.context.ApplicationContext;

/**
 * @author erzbir
 * @since 1.0.0
 */
public class ApplicationContextEvent extends ApplicationEvent {

    public ApplicationContextEvent(Object source) {
        super(source);
    }

    public final ApplicationContext getApplicationContext() {
        return (ApplicationContext) getSource();
    }
}

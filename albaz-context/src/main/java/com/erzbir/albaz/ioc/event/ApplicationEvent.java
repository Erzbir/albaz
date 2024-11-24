package com.erzbir.albaz.ioc.event;

import java.util.EventObject;

/**
 * @author erzbir
 * @since 1.0.0
 */
public abstract class ApplicationEvent extends EventObject {

    public ApplicationEvent(Object source) {
        super(source);
    }
}

package com.erzbir.di.event;

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

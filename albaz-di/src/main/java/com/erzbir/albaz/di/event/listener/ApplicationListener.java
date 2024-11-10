package com.erzbir.albaz.di.event.listener;


import com.erzbir.albaz.di.event.ApplicationEvent;

import java.util.EventListener;

/**
 * @author erzbir
 * @since 1.0.0
 */
public interface ApplicationListener<E extends ApplicationEvent> extends EventListener {

    void onApplicationEvent(E event);
}

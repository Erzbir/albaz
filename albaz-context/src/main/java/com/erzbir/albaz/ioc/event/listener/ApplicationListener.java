package com.erzbir.albaz.ioc.event.listener;


import com.erzbir.albaz.ioc.event.ApplicationEvent;

import java.util.EventListener;

/**
 * @author erzbir
 * @since 1.0.0
 */
public interface ApplicationListener<E extends ApplicationEvent> extends EventListener {

    void onApplicationEvent(E event);
}

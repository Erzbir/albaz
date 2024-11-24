package com.erzbir.albaz.ioc.event.publisher;


import com.erzbir.albaz.ioc.event.ApplicationEvent;

/**
 * @author erzbir
 * @since 1.0.0
 */
public interface ApplicationEventPublisher {

    void publishEvent(ApplicationEvent event);
}

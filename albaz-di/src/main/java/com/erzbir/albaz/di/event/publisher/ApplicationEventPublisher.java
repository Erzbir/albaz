package com.erzbir.albaz.di.event.publisher;


import com.erzbir.albaz.di.event.ApplicationEvent;

/**
 * @author erzbir
 * @since 1.0.0
 */
public interface ApplicationEventPublisher {

    void publishEvent(ApplicationEvent event);
}

package com.erzbir.di.event.publisher;


import com.erzbir.di.event.ApplicationEvent;

/**
 * @author erzbir
 * @since 1.0.0
 */
public interface ApplicationEventPublisher {

    void publishEvent(ApplicationEvent event);
}

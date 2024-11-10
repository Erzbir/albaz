package com.erzbir.albaz.di.event.broadcast;


import com.erzbir.albaz.di.beans.factory.ConfigurableBeanFactory;
import com.erzbir.albaz.di.event.ApplicationEvent;
import com.erzbir.albaz.di.event.listener.ApplicationListener;

/**
 * @author erzbir
 * @since 1.0.0
 */
public class SimpleApplicationEventBroadcaster extends AbstractApplicationEventBroadcaster {

    public SimpleApplicationEventBroadcaster(ConfigurableBeanFactory beanFactory) {
        setBeanFactory(beanFactory);
    }

    @Override
    public void broadcastEvent(ApplicationEvent event) {
        for (ApplicationListener listener : getApplicationListeners(event)) {
            listener.onApplicationEvent(event);
        }
    }
}

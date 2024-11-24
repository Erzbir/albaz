package com.erzbir.albaz.ioc.event.broadcast;


import com.erzbir.albaz.ioc.beans.factory.ConfigurableBeanFactory;
import com.erzbir.albaz.ioc.event.ApplicationEvent;
import com.erzbir.albaz.ioc.event.listener.ApplicationListener;

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

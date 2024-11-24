package com.erzbir.albaz.ioc.context;


import com.erzbir.albaz.ioc.beans.factory.HierarchicalBeanFactory;
import com.erzbir.albaz.ioc.beans.factory.ListableBeanFactory;
import com.erzbir.albaz.ioc.event.publisher.ApplicationEventPublisher;
import com.erzbir.albaz.ioc.io.loader.ResourceLoader;

/**
 * @author erzbir
 * @since 1.0.0
 */
public interface ApplicationContext extends ListableBeanFactory, HierarchicalBeanFactory, ResourceLoader, ApplicationEventPublisher {
    String getDisplayName();
}

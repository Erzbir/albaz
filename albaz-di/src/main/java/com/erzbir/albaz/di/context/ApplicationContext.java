package com.erzbir.albaz.di.context;


import com.erzbir.albaz.di.beans.factory.HierarchicalBeanFactory;
import com.erzbir.albaz.di.beans.factory.ListableBeanFactory;
import com.erzbir.albaz.di.event.publisher.ApplicationEventPublisher;
import com.erzbir.albaz.di.io.loader.ResourceLoader;

/**
 * @author erzbir
 * @since 1.0.0
 */
public interface ApplicationContext extends ListableBeanFactory, HierarchicalBeanFactory, ResourceLoader, ApplicationEventPublisher {
    String getDisplayName();
}

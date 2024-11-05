package com.erzbir.di.context;


import com.erzbir.di.beans.factory.HierarchicalBeanFactory;
import com.erzbir.di.beans.factory.ListableBeanFactory;
import com.erzbir.di.event.publisher.ApplicationEventPublisher;
import com.erzbir.di.io.loader.ResourceLoader;

/**
 * @author erzbir
 * @since 1.0.0
 */
public interface ApplicationContext extends ListableBeanFactory, HierarchicalBeanFactory, ResourceLoader, ApplicationEventPublisher {
    String getDisplayName();
}

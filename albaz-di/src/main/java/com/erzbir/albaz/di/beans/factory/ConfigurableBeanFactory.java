package com.erzbir.albaz.di.beans.factory;


import com.erzbir.albaz.di.beans.annotation.support.StringValueResolver;
import com.erzbir.albaz.di.beans.registry.SingletonBeanRegistry;

/**
 * @author erzbir
 * @since 1.0.0
 */
public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry {

    String SCOPE_SINGLETON = "singleton";

    String SCOPE_PROTOTYPE = "prototype";

    void destroySingletons();

    void addEmbeddedValueResolver(StringValueResolver valueResolver);

    String resolveEmbeddedValue(String value);
}

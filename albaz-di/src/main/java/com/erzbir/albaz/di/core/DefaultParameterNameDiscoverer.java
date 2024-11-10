package com.erzbir.albaz.di.core;

/**
 * @author erzbir
 * @since 1.0.0
 */
public class DefaultParameterNameDiscoverer extends PrioritizedParameterNameDiscoverer {

    public DefaultParameterNameDiscoverer() {
        addDiscoverer(new StandardReflectionParameterNameDiscoverer());
    }

}
package com.erzbir.albaz.ioc.beans.factory;

/**
 * @author erzbir
 * @since 1.0.0
 */
public interface ObjectFactory<T> {

    T getObject();
}

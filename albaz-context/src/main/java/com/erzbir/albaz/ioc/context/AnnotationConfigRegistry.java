package com.erzbir.albaz.ioc.context;

/**
 * @author erzbir
 * @since 1.0.0
 */
public interface AnnotationConfigRegistry {

    void register(Class<?>... componentClasses);

    void scan(String... basePackages);

}

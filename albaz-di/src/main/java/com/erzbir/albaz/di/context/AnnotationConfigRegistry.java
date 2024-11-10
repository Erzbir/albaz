package com.erzbir.albaz.di.context;

/**
 * @author erzbir
 * @since 1.0.0
 */
public interface AnnotationConfigRegistry {

    void register(Class<?>... componentClasses);

    void scan(String... basePackages);

}

package com.erzbir.albaz.ioc.beans.annotation.register;


import com.erzbir.albaz.ioc.beans.annotation.Component;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author erzbir
 * @since 1.0.0
 */
public abstract class AbstractAnnotationRegistryFactory implements AnnotationRegistryFactory {

    /**
     * 缓存所有需要扫描的注解
     */
    Set<Class<? extends Annotation>> ANNOTATION_MAP;

    {
        ANNOTATION_MAP = new LinkedHashSet<>();
        addAnnotation(new AnnotationType(Component.class));

    }

    @Override
    public void addAnnotation(AnnotationType annotationType) {
        ANNOTATION_MAP.add(annotationType.getAnnotation());
    }

    @Override
    public List<Class<? extends Annotation>> getAllAnnotations() {
        return new ArrayList<>(ANNOTATION_MAP);
    }


}

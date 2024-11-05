package com.erzbir.di.beans.annotation.register;

import java.lang.annotation.Annotation;

/**
 * @author erzbir
 * @since 1.0.0
 */
public final class AnnotationType {
    private final Class<? extends Annotation> annotation;

    public AnnotationType(Class<? extends Annotation> annotation) {
        this.annotation = annotation;
    }

    public Class<? extends Annotation> getAnnotation() {
        return annotation;
    }
}

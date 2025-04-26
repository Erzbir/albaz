package com.erzbir.albaz.starter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public interface AnnotationHandler<A extends Annotation> {
    Class<A> getAnnotationType();

    void handle(Object bean, Method method, A annotation);
}
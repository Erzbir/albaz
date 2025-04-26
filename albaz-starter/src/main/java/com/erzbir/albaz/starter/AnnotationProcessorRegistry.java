package com.erzbir.albaz.starter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class AnnotationProcessorRegistry {
    private final Map<Class<? extends Annotation>, AnnotationHandler<?>> handlerMap = new HashMap<>();

    public void registerHandler(AnnotationHandler<?> handler) {
        handlerMap.put(handler.getAnnotationType(), handler);
    }

    public void process(Object bean) {
        Class<?> clazz = bean.getClass();

        for (Method method : clazz.getDeclaredMethods()) {
            for (Annotation annotation : method.getAnnotations()) {
                AnnotationHandler handler = handlerMap.get(annotation.annotationType());
                if (handler != null) {
                    method.setAccessible(true);
                    handler.handle(bean, method, annotation);
                }
            }
        }
    }
}
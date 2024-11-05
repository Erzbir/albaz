package com.erzbir.di.filter;


import com.erzbir.di.enums.AnnotationEnum;
import com.erzbir.util.ConcurrentHashSet;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * @author erzbir
 * @since 1.0.0
 */
public class AnnotationTypeFilter {

    /**
     * save all annotations
     */
    private static Set<Class<? extends Annotation>> ANNOTATION_MAP = new ConcurrentHashSet<>();

    public AnnotationTypeFilter() {
        ANNOTATION_MAP.addAll(AnnotationEnum.getAnnotations());
    }

    /**
     * 添加需要扫描的Annotation
     *
     * @param annotation
     */
    public static void addAnnotationType(Class<? extends Annotation> annotation) {
        ANNOTATION_MAP.add(annotation);
    }

    /**
     * 获得需要扫描的Annotation
     *
     * @return
     */
    public static Set<Class<? extends Annotation>> getAllAnnotationTypes() {
        return ANNOTATION_MAP;
    }
}

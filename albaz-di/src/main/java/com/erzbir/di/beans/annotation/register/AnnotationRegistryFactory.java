package com.erzbir.di.beans.annotation.register;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * @author erzbir
 * @since 1.0.0
 */
public interface AnnotationRegistryFactory {

    /**
     * 对外提供添加需要扫描的注解的接口
     */
    void registerAnnotations();


    /**
     * 获得所有需要扫描的注解
     */
    List<Class<? extends Annotation>> getAllAnnotations();


    /**
     * 添加所有注解
     */
    void addAnnotation(AnnotationType annotationType);
}
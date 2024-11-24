package com.erzbir.albaz.ioc.beans.annotation.scanner;


import com.erzbir.albaz.ioc.beans.po.BeanDefinition;
import com.erzbir.albaz.util.ClassScanner;

import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author erzbir
 * @since 1.0.0
 */
public class ClassPathScanningCandidateBeanProvider {


    /**
     * 扫描basePackage包下的所有带Bean注解的类
     *
     * @param basePackage
     * @return
     */
    public Set<BeanDefinition> findCandidateBeans(String basePackage, List<Class<? extends Annotation>> annotationList) {
        Set<BeanDefinition> candidates = new LinkedHashSet<>();

        //扫描basePackage包下的所有带注解的类 包括@Bean 和自定义注解
        for (Class<? extends Annotation> annotation : annotationList) {
            Set<Class<?>> classes = ClassScanner.scanPackageByAnnotation(basePackage, annotation);
            for (Class<?> clazz : classes) {
                candidates.add(new BeanDefinition(clazz));
            }
        }
        return candidates;
    }
}

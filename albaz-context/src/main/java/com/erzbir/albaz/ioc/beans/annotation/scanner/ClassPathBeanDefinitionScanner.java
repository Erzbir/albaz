package com.erzbir.albaz.ioc.beans.annotation.scanner;


import com.erzbir.albaz.ioc.beans.annotation.Component;
import com.erzbir.albaz.ioc.beans.annotation.Scope;
import com.erzbir.albaz.ioc.beans.annotation.register.AnnotationRegistryFactory;
import com.erzbir.albaz.ioc.beans.po.BeanDefinition;
import com.erzbir.albaz.ioc.beans.processor.support.AutowiredAndValueAnnotationBeanPostProcessor;
import com.erzbir.albaz.ioc.beans.registry.BeanDefinitionRegistry;
import com.erzbir.albaz.ioc.enums.ScopeEnum;
import com.erzbir.albaz.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author erzbir
 * @since 1.0.0
 */
public class ClassPathBeanDefinitionScanner extends ClassPathScanningCandidateBeanProvider {

    private BeanDefinitionRegistry registry;
    private String ANNOTATION_REGISTRY_FACTORY_NAME = "annotationRegistryFactory";

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public void scan(String... basePackages) {
        for (String basePackage : basePackages) {
            BeanDefinition annotationRegistryFactoryBeanDefinition = registry.getBeanDefinition(ANNOTATION_REGISTRY_FACTORY_NAME);
            List<Class<? extends Annotation>> allAnnotations = null;
            if (annotationRegistryFactoryBeanDefinition != null) {
                try {
                    AnnotationRegistryFactory annotationRegistryFactory = (AnnotationRegistryFactory) annotationRegistryFactoryBeanDefinition.getBeanClass().getConstructor().newInstance();
                    annotationRegistryFactory.registerAnnotations();
                    allAnnotations = annotationRegistryFactory.getAllAnnotations();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException e) {
                    throw new RuntimeException("AnnotationRegistryFactory failed to be registered: ", e);
                }
            }
            // 如果没有配置 则添加 @Component 注解
            if (allAnnotations == null) allAnnotations = new LinkedList<>(Collections.singleton(Component.class));
            Set<BeanDefinition> candidates = findCandidateBeans(basePackage, allAnnotations);
            for (BeanDefinition beanDefinition : candidates) {
                //解析BeanDefinition的作用域
                ScopeEnum scopeEnum = resolveBeanScope(beanDefinition);
                // 默认单例
                if (scopeEnum == null) scopeEnum = ScopeEnum.SINGLETON;
                beanDefinition.setScope(scopeEnum.getTypes());

                registry.registerBeanDefinition(determineBeanName(beanDefinition), beanDefinition);
            }
        }

        registry.registerBeanDefinition("com.erzbir.beans.processor.support.AutowiredAndValueAnnotationBeanPostProcessor", new BeanDefinition(AutowiredAndValueAnnotationBeanPostProcessor.class));

    }

    private ScopeEnum resolveBeanScope(BeanDefinition beanDefinition) {
        Class<?> beanClass = beanDefinition.getBeanClass();
        Scope scopeAnnotation = beanClass.getAnnotation(Scope.class);
        if (scopeAnnotation == null) return null;
        return scopeAnnotation.value();
    }

    private String determineBeanName(BeanDefinition beanDefinition) {
        Class<?> beanClass = beanDefinition.getBeanClass();
        String value = null;

        Component componentAnnotation = beanClass.getAnnotation(Component.class);
        if (componentAnnotation != null) {
            value = componentAnnotation.value();
        }
        if (!StringUtils.hasLength(value)) {
            value = StringUtils.lowerFirst(beanClass.getSimpleName());
        }
        return value;
    }
}

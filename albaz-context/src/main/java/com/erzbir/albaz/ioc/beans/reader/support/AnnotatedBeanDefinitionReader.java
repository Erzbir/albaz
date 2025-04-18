package com.erzbir.albaz.ioc.beans.reader.support;

import com.erzbir.albaz.ioc.beans.annotation.ComponentScan;
import com.erzbir.albaz.ioc.beans.annotation.scanner.ClassPathBeanDefinitionScanner;
import com.erzbir.albaz.ioc.beans.po.BeanDefinition;
import com.erzbir.albaz.ioc.beans.registry.BeanDefinitionRegistry;
import com.erzbir.albaz.ioc.io.loader.ResourceLoader;
import com.erzbir.albaz.util.StringUtils;
import org.jetbrains.annotations.Nullable;

/**
 * @author erzbir
 * @since 1.0.0
 */
public class AnnotatedBeanDefinitionReader {
    private final BeanDefinitionRegistry registry;

    public AnnotatedBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public AnnotatedBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader resourceLoader) {
        this.registry = registry;
    }

    public final BeanDefinitionRegistry getRegistry() {
        return this.registry;
    }

    public void register(Class<?>... componentClasses) {
        for (Class<?> componentClass : componentClasses) {
            ComponentScan annotation = componentClass.getAnnotation(ComponentScan.class);
            String basePackage;
            if (annotation != null) {
                basePackage = annotation.value();
            } else {
                basePackage = componentClass.getPackage().getName();
            }
            if (!StringUtils.hasText(basePackage)) {
                return;
            }
            scanPackage(basePackage);
        }
    }

    private void scanPackage(String scnPath) {
        String[] basePackages = StringUtils.delimitedListToStringArray(scnPath, ",");
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(getRegistry());
        scanner.scan(basePackages);
    }

    public <T> void registerBean(@Nullable String beanName, Class<T> beanClass) {
        BeanDefinition beanDefinition = new BeanDefinition(beanClass);
        registry.registerBeanDefinition(beanName, beanDefinition);
    }

}

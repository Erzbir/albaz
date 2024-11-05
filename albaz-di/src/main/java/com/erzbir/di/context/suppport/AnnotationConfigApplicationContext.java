package com.erzbir.di.context.suppport;

import com.erzbir.di.beans.annotation.scanner.ClassPathBeanDefinitionScanner;
import com.erzbir.di.beans.factory.ConfigurableListableBeanFactory;
import com.erzbir.di.beans.factory.support.DefaultListableBeanFactory;
import com.erzbir.di.beans.po.BeanDefinition;
import com.erzbir.di.beans.reader.support.AnnotatedBeanDefinitionReader;
import com.erzbir.di.beans.registry.BeanDefinitionRegistry;
import com.erzbir.di.context.AnnotationConfigRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author erzbir
 * @since 1.0.0
 */
public class AnnotationConfigApplicationContext extends AbstractApplicationContext implements AnnotationConfigRegistry, BeanDefinitionRegistry {
    private final AnnotatedBeanDefinitionReader reader;
    private final ClassPathBeanDefinitionScanner scanner;
    private final AtomicBoolean refreshed = new AtomicBoolean();
    private DefaultListableBeanFactory beanFactory;
    private List<String> packageNames;
    private List<Class<?>> classes;

    public AnnotationConfigApplicationContext() {
        this.scanner = new ClassPathBeanDefinitionScanner(this);
        this.reader = new AnnotatedBeanDefinitionReader(this);
        this.beanFactory = new DefaultListableBeanFactory();
        this.packageNames = new ArrayList<>();
        this.classes = new ArrayList<>();
    }

    public AnnotationConfigApplicationContext(Class<?>... componentClasses) {
        this();
        register(componentClasses);
        refresh();
    }


    public AnnotationConfigApplicationContext(String... basePackages) {
        this();
        scan(basePackages);
        refresh();
    }


    @Override
    public void register(Class<?>... componentClasses) {
        classes.addAll(List.of(componentClasses));
        reader.register(componentClasses);
    }


    @Override
    public void scan(String... basePackages) {
//        Assert.notEmpty(basePackages, "At least one base package must be specified");
        packageNames.addAll(List.of(basePackages));
        this.scanner.scan(basePackages);

    }

    public <T> void registerBean(String beanName, Class<T> beanClass) {
        this.reader.registerBean(beanName, beanClass);
    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanFactory.registerBeanDefinition(beanName, beanDefinition);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) {
        return beanFactory.getBeanDefinition(beanName);
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return beanFactory.containsBeanDefinition(beanName);
    }

    @Override
    protected void refreshBeanFactory() {
        if (!this.refreshed.compareAndSet(false, true)) {
            throw new IllegalStateException(
                    "GenericApplicationContext does not support multiple refresh attempts: just call 'refresh' once");
        }
    }

    @Override
    public ConfigurableListableBeanFactory getBeanFactory() {
        return beanFactory;
    }

    @Override
    public List<Class<?>> getAllBeanClass() {
        return beanFactory.getAllBeanClass();
    }
}
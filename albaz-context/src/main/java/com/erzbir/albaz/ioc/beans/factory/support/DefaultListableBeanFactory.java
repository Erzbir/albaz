package com.erzbir.albaz.ioc.beans.factory.support;


import com.erzbir.albaz.ioc.beans.factory.ConfigurableListableBeanFactory;
import com.erzbir.albaz.ioc.beans.po.BeanDefinition;
import com.erzbir.albaz.ioc.beans.registry.BeanDefinitionRegistry;
import com.erzbir.albaz.ioc.exception.BeanException;
import com.erzbir.albaz.logging.Log;
import com.erzbir.albaz.logging.LogFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author erzbir
 * @since 1.0.0
 */
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements BeanDefinitionRegistry, ConfigurableListableBeanFactory {
    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
    private Log log = LogFactory.getLog(DefaultListableBeanFactory.class);

    @Override
    public BeanDefinition getBeanDefinition(String beanName) {

        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (beanDefinition == null) {
            log.warn("The bean named" + beanName + "is not found");
        }
        return beanDefinition;
    }

    @Override
    public void preInstantiateSingletons() {
        beanDefinitionMap.keySet().forEach(this::getBean);
    }


    @Override
    public <T> T getBean(Class<T> requiredType) {
        List<String> beanNames = new ArrayList<>();
        Set<Map.Entry<String, BeanDefinition>> entries = beanDefinitionMap.entrySet();
        for (Map.Entry entry : entries) {
            BeanDefinition beanDefinition = (BeanDefinition) entry.getValue();
            Class<?> clazz = beanDefinition.getBeanClass();
            if (requiredType.isAssignableFrom(clazz)) {
                beanNames.add((String) entry.getKey());
            }
        }
        if (beanNames.isEmpty()) return getBean(beanNames.getFirst(), requiredType);

        throw new BeanException(requiredType + "expected single bean but found " + beanNames.size() + ": " + beanNames);
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return beanDefinitionMap.containsKey(beanName);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) {
        Map<String, T> res = new HashMap<>();
        beanDefinitionMap.forEach((beanName, beanDefinition) -> {
            Class<?> clazz = beanDefinition.getBeanClass();
            if (type.isAssignableFrom(clazz)) {
                // 讲扫描到的processor添加到缓存，processor这时还未创建
                // 通过getBean创建对象
                res.put(beanName, (T) getBean(beanName));
            }
        });

        return res;
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return beanDefinitionMap.keySet().toArray(new String[0]);
    }


    @Override
    public void registerBeanDefinition(String name, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(name, beanDefinition);
    }

    public List<Class<?>> getAllBeanClass() {
        return beanDefinitionMap.values()
                .stream()
                .map((Function<BeanDefinition, Class<?>>) BeanDefinition::getBeanClass)
                .collect(Collectors.toList());
    }

}

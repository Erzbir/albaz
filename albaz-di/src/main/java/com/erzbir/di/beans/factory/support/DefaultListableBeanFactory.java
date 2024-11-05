package com.erzbir.di.beans.factory.support;


import com.erzbir.di.beans.factory.ConfigurableListableBeanFactory;
import com.erzbir.di.beans.po.BeanDefinition;
import com.erzbir.di.beans.registry.BeanDefinitionRegistry;
import com.erzbir.di.exception.BeanException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author erzbir
 * @since 1.0.0
 */
@Slf4j
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements BeanDefinitionRegistry, ConfigurableListableBeanFactory {
    private final Log logger = LogFactory.getLog(this.getClass());
    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    @Override
    public BeanDefinition getBeanDefinition(String beanName) {

        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (beanDefinition == null) {
            logger.warn("The bean named " + beanName + " is not found");
        }
        return beanDefinition;
    }

    @Override
    public void preInstantiateSingletons() {
        beanDefinitionMap.keySet().forEach(beanName -> getBean(beanName));
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

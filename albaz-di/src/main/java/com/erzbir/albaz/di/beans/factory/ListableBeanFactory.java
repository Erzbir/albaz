package com.erzbir.albaz.di.beans.factory;

import java.util.List;
import java.util.Map;

/**
 * @author erzbir
 * @since 1.0.0
 */
public interface ListableBeanFactory extends BeanFactory {

    <T> Map<String, T> getBeansOfType(Class<T> type);

    String[] getBeanDefinitionNames();

    List<Class<?>> getAllBeanClass();
}

package com.erzbir.albaz.di.aop.advisor;

import java.lang.reflect.Method;

/**
 * @author erzbir
 * @since 1.0.0
 */
public interface MethodMatcher {

    boolean matches(Method method, Class<?> targetClass);

}

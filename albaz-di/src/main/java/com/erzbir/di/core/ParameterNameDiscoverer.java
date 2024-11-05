package com.erzbir.di.core;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * @author erzbir
 * @since 1.0.0
 */
public interface ParameterNameDiscoverer {

    @Nullable
    String[] getParameterNames(Method method);

    @Nullable
    String[] getParameterNames(Constructor<?> ctor);

}
package com.erzbir.albaz.di.core;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author erzbir
 * @since 1.0.0
 */
public class StandardReflectionParameterNameDiscoverer implements ParameterNameDiscoverer {

    @Override
    @Nullable
    public String[] getParameterNames(Method method) {
        return getParameterNames(method.getParameters());
    }

    @Override
    @Nullable
    public String[] getParameterNames(Constructor<?> ctor) {
        return getParameterNames(ctor.getParameters());
    }

    @Nullable
    private String[] getParameterNames(Parameter[] parameters) {
        String[] parameterNames = new String[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            if (!param.isNamePresent()) {
                return null;
            }
            parameterNames[i] = param.getName();
        }
        return parameterNames;
    }

}

package com.erzbir.albaz.di.aop;

import java.lang.reflect.InvocationTargetException;

/**
 * @author erzbir
 * @since 1.0.0
 */
public class PrototypeAspectInstanceFactory implements AspectInstanceFactory {

    private Class<?> clazz;

    public PrototypeAspectInstanceFactory(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Object getAspectInstance() {
        try {
            return clazz.getConstructor().newInstance();
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException |
                 IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}

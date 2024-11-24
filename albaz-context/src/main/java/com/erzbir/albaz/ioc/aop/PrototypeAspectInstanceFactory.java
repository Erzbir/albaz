package com.erzbir.albaz.ioc.aop;


import com.erzbir.albaz.logging.Log;
import com.erzbir.albaz.logging.LogFactory;

import java.lang.reflect.InvocationTargetException;

/**
 * @author erzbir
 * @since 1.0.0
 */
public class PrototypeAspectInstanceFactory implements AspectInstanceFactory {

    private final Class<?> clazz;
    private final Log log = LogFactory.getLog(PrototypeAspectInstanceFactory.class);

    public PrototypeAspectInstanceFactory(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Object getAspectInstance() {
        try {
            return clazz.getConstructor().newInstance();
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException |
                 IllegalAccessException e) {
            log.error("Aspect instance creation failed", e);
        }
        return null;
    }
}

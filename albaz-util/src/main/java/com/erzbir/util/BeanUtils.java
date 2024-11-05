package com.erzbir.util;

import java.lang.reflect.Field;

public abstract class BeanUtils {
    public static void setFieldValue(Object bean, Field field, Object value) {
        ReflectionUtils.setField(field, bean, value);

    }

    public static void setFieldValue(Object bean, String field, Object value) {
        ReflectionUtils.setField(field, bean, value);

    }
}

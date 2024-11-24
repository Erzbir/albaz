package com.erzbir.albaz.ioc.enums;


import com.erzbir.albaz.ioc.beans.annotation.Autowired;
import com.erzbir.albaz.ioc.beans.annotation.Component;
import com.erzbir.albaz.ioc.beans.annotation.ComponentScan;
import com.erzbir.albaz.ioc.beans.annotation.Scope;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * @author erzbir
 * @since 1.0.0
 */
public enum AnnotationEnum {


    AUTOWIRED(Autowired.class),
    BEAN(Component.class),
    BEANSCAN(ComponentScan.class),
    SCOPE(Scope.class);

    private final Class<? extends Annotation> value;

    AnnotationEnum(Class<? extends Annotation> value) {
        this.value = value;
    }

    public static List<Class<? extends Annotation>> getAnnotations() {
        List<Class<? extends Annotation>> list = new ArrayList<>();

        list.add(AUTOWIRED.getValue());
        list.add(BEAN.getValue());
        list.add(BEANSCAN.getValue());
        list.add(SCOPE.getValue());
        return list;
    }

    public Class<? extends Annotation> getValue() {
        return value;
    }
}

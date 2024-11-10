package com.erzbir.albaz.di.enums;


import com.erzbir.albaz.di.beans.annotation.Autowired;
import com.erzbir.albaz.di.beans.annotation.Component;
import com.erzbir.albaz.di.beans.annotation.ComponentScan;
import com.erzbir.albaz.di.beans.annotation.Scope;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * @author erzbir
 * @since 1.0.0
 */
@AllArgsConstructor
@Getter
public enum AnnotationEnum {


    AUTOWIRED(Autowired.class),
    BEAN(Component.class),
    BEANSCAN(ComponentScan.class),
    SCOPE(Scope.class);

    private final Class<? extends Annotation> value;

    public static List<Class<? extends Annotation>> getAnnotations() {
        List<Class<? extends Annotation>> list = new ArrayList<>();

        list.add(AUTOWIRED.getValue());
        list.add(BEAN.getValue());
        list.add(BEANSCAN.getValue());
        list.add(SCOPE.getValue());
        return list;
    }
}

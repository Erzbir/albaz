package com.erzbir.di.beans.annotation;

import java.lang.annotation.*;

/**
 * @author erzbir
 * @since 1.0.0
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Value {

    String value();
}

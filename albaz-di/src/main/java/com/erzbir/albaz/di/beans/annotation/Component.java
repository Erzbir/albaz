package com.erzbir.albaz.di.beans.annotation;

import java.lang.annotation.*;

/**
 * @author erzbir
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Component {

    String value() default "";
}

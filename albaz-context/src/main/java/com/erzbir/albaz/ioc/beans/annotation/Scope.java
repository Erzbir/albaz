package com.erzbir.albaz.ioc.beans.annotation;


import com.erzbir.albaz.ioc.enums.ScopeEnum;

import java.lang.annotation.*;

/**
 * @author erzbir
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Scope {

    ScopeEnum value() default ScopeEnum.SINGLETON;
}

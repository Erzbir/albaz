package com.erzbir.albaz.ioc.beans.annotation;

import java.lang.annotation.*;

/**
 * @author erzbir
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface Autowired {
}

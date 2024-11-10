package com.erzbir.albaz.di.aop.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author erzbir
 * @since 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AfterThrowing {
    String value() default "";

    /**
     * 值只能是 ex，或者异常的全限定名，如 java.lang.ClassNotFoundException
     * 如果注解中指定了 throwing 参数，通知方法中第一个参数必须是异常类型
     *
     * @return
     */
    String throwing() default "";
}

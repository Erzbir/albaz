package com.erzbir.di.aop.advisor;

/**
 * @author erzbir
 * @since 1.0.0
 */
public interface Pointcut {

    MethodMatcher getMethodMatcher();

}

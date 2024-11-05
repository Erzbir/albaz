package com.erzbir.di.aop.advisor;

/**
 * @author erzbir
 * @since 1.0.0
 */
public interface Joinpoint {

    Object proceed() throws Throwable;

}

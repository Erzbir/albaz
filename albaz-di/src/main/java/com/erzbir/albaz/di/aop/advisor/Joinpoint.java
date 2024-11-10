package com.erzbir.albaz.di.aop.advisor;

/**
 * @author erzbir
 * @since 1.0.0
 */
public interface Joinpoint {

    Object proceed() throws Throwable;

}

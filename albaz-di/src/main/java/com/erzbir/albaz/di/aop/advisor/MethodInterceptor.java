package com.erzbir.albaz.di.aop.advisor;

/**
 * @author erzbir
 * @since 1.0.0
 */
public interface MethodInterceptor extends Interceptor {

    Object invoke(MethodInvocation invocation) throws Throwable;
}

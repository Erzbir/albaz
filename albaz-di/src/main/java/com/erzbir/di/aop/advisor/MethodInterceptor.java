package com.erzbir.di.aop.advisor;

/**
 * @author erzbir
 * @since 1.0.0
 */
public interface MethodInterceptor extends Interceptor {

    Object invoke(MethodInvocation invocation) throws Throwable;
}

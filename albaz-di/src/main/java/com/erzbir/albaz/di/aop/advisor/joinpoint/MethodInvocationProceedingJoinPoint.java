package com.erzbir.albaz.di.aop.advisor.joinpoint;


import com.erzbir.albaz.di.aop.advisor.MethodInvocation;

/**
 * @author erzbir
 * @since 1.0.0
 */
public class MethodInvocationProceedingJoinPoint implements ProceedingJoinPoint {

    private final MethodInvocation methodInvocation;

    public MethodInvocationProceedingJoinPoint(MethodInvocation mi) {
        this.methodInvocation = mi;
    }

    @Override
    public Object proceed() throws Throwable {
        return this.methodInvocation.proceed();
    }

    @Override
    public Object proceed(Object[] args) throws Throwable {
        this.methodInvocation.setArguments(args);
        return this.methodInvocation.proceed();
    }

    @Override
    public Object[] getArgs() {
        return this.methodInvocation.getArguments().clone();
    }

    @Override
    public String getMethodName() {
        return this.methodInvocation.getMethod().getName();
    }
}

package com.erzbir.di.aop.advisor.joinpoint;

/**
 * @author erzbir
 * @since 1.0.0
 */
public interface JoinPoint {

    /**
     * 获取执行链中目标方法的实参
     *
     * @return
     */
    Object[] getArgs();

    /**
     * 获取执行链中目标方法的方法名
     *
     * @return
     */
    String getMethodName();

}

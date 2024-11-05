package com.erzbir.di.aop.advisor;

import java.lang.reflect.Method;

/**
 * @author erzbir
 * @since 1.0.0
 */
public interface MethodInvocation extends Joinpoint {

    /**
     * 获得执行链中目标方法的实参
     *
     * @return
     */
    Object[] getArguments();

    /**
     * 修改执行链中目标方法的实参
     * ProxyMethodInvocation 中的功能，这里直接放在 MethodInvocation 中了，允许修改实参
     */
    void setArguments(Object[] args);

    /**
     * 获得执行链中的目标方法
     *
     * @return
     */
    Method getMethod();

}

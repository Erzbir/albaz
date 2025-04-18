package com.erzbir.albaz.ioc.aop.anno;


import com.erzbir.albaz.ioc.aop.AspectInstanceFactory;
import com.erzbir.albaz.ioc.aop.advisor.Advice;
import com.erzbir.albaz.ioc.aop.advisor.CommonAdvice;
import com.erzbir.albaz.ioc.aop.advisor.MethodInterceptor;
import com.erzbir.albaz.ioc.aop.advisor.MethodInvocation;

import java.lang.reflect.Method;

/**
 * @author erzbir
 * @since 1.0.0
 */
public class AfterThrowingAdvice extends CommonAdvice implements Advice, MethodInterceptor {

    public AfterThrowingAdvice(Method aspectJAdviceMethod, AspectInstanceFactory aspectInstanceFactory) {
        super(aspectJAdviceMethod, aspectInstanceFactory);
    }

    @Override
    public void setThrowingName(String name) {
        super.setThrowingName(name);
    }

    /*
     public Object invoke(MethodInvocation mi) throws Throwable {
		try {
			return mi.proceed();
		}
		catch (Throwable ex) {
			if (shouldInvokeOnThrowing(ex)) {
				invokeAdviceMethod(getJoinPointMatch(), null, ex);
			}
			throw ex;
		}
	}
     */

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            return invocation.proceed();
        } catch (Throwable ex) {
            if (shouldInvokeOnThrowing(ex)) {
                afterThrowing(ex);
            }
            throw ex;
        }
    }

    /**
     * 只有当抛出的异常是给定抛出类型的子类型时，才会调用 afterThrowing 通知。
     *
     * @param ex
     * @return
     */
    private boolean shouldInvokeOnThrowing(Throwable ex) {
        return getDiscoveredThrowingType().isAssignableFrom(ex.getClass());
    }

    public void afterThrowing(Throwable ex) throws Throwable {
        invokeAdviceMethod(null, ex);
    }

    @Override
    public int getOrder() {
        return 2;
    }

}

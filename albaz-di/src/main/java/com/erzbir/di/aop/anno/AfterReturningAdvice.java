package com.erzbir.di.aop.anno;


import com.erzbir.di.aop.AspectInstanceFactory;
import com.erzbir.di.aop.advisor.Advice;
import com.erzbir.di.aop.advisor.CommonAdvice;
import com.erzbir.di.aop.advisor.MethodInterceptor;
import com.erzbir.di.aop.advisor.MethodInvocation;

import java.lang.reflect.Method;

/**
 * @author erzbir
 * @since 1.0.0
 */
public class AfterReturningAdvice extends CommonAdvice implements Advice, MethodInterceptor {

    public AfterReturningAdvice(Method aspectJAdviceMethod, AspectInstanceFactory aspectInstanceFactory) {
        super(aspectJAdviceMethod, aspectInstanceFactory);
    }

    /*
    	@Override
	@Nullable
	public Object invoke(MethodInvocation mi) throws Throwable {
		Object retVal = mi.proceed();
		this.advice.afterReturning(retVal, mi.getMethod(), mi.getArguments(), mi.getThis());
		return retVal;
	}

     */

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object retVal = invocation.proceed();
        afterReturning();
        return retVal;
    }

    public void afterReturning() throws Throwable {
        invokeAdviceMethod(null, null);
    }

    @Override
    public int getOrder() {
        return 2;
    }
}

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
public class BeforeAdvice extends CommonAdvice implements Advice, MethodInterceptor {

    public BeforeAdvice(Method aspectJAdviceMethod, AspectInstanceFactory aspectInstanceFactory) {
        super(aspectJAdviceMethod, aspectInstanceFactory);
    }

    /*
    	public Object invoke(MethodInvocation mi) throws Throwable {
		this.advice.before(mi.getMethod(), mi.getArguments(), mi.getThis());
		return mi.proceed();
	}
     */

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        before();
        return invocation.proceed();
    }

    public void before() throws Throwable {
        invokeAdviceMethod(null, null);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}

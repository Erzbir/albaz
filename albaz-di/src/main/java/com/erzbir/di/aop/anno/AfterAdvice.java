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
public class AfterAdvice extends CommonAdvice implements Advice, MethodInterceptor {

    public AfterAdvice(Method aspectJAdviceMethod, AspectInstanceFactory aspectInstanceFactory) {
        super(aspectJAdviceMethod, aspectInstanceFactory);
    }

    /*
    	@Override
	@Nullable
	public Object invoke(MethodInvocation mi) throws Throwable {
		try {
			return mi.proceed();
		}
		finally {
			invokeAdviceMethod(getJoinPointMatch(), null, null);
		}
	}
     */

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            return invocation.proceed();
        } finally {
            after();
        }
    }

    public void after() throws Throwable {
        invokeAdviceMethod(null, null);
    }

    @Override
    public int getOrder() {
        return 1;
    }
}

package com.erzbir.albaz.di.aop.proxy;

import com.erzbir.albaz.di.aop.advisor.DefaultMethodInvocation;
import com.erzbir.albaz.di.aop.advisor.Interceptor;
import com.erzbir.albaz.di.aop.util.AopUtils;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public class ObjenesisCglibAopProxy implements AopProxy, MethodInterceptor {

    private ProxyFactory proxyFactory;

    public ObjenesisCglibAopProxy(ProxyFactory proxyFactory) {
        this.proxyFactory = proxyFactory;
    }

    @Override
    public Object getProxy() {
        Enhancer enhancer = new Enhancer();
        TargetSource targetSource = proxyFactory.getTargetSource();
        Object target = targetSource.getTarget();
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        Object oldProxy = null;
        boolean setProxyContext = false;

        TargetSource targetSource = this.proxyFactory.getTargetSource();
        Object target = null;

        try {
            Object retVal;

            // 暴露当前正在运行的代理对象给 AopContext
            if (this.proxyFactory.exposeProxy()) {
                oldProxy = AopContext.setCurrentProxy(proxy);
                setProxyContext = true;
            }

            target = targetSource.getTarget();
            Class<?> targetClass = target.getClass();

            // 得到此 method 的拦截器链，就是一堆环绕通知
            // 需要根据 invoke 的 method 来做进一步确定，过滤出应用在这个 method 上的 Advice
            List<Interceptor> chain = this.proxyFactory.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);

            if (chain.isEmpty()) {
                retVal = AopUtils.invokeJoinpointUsingReflection(target, method, args);
            } else {
                DefaultMethodInvocation methodInvocation = new DefaultMethodInvocation(target, method, args, chain);
                retVal = methodInvocation.proceed();
            }

            // 处理特殊的返回值 this
            Class<?> returnType = method.getReturnType();
            if (retVal != null && retVal == target &&
                    returnType != Object.class && returnType.isInstance(proxy)) {
                retVal = proxy;
            }
            return retVal;
        } finally {
            if (setProxyContext) {
                // Restore old proxy.
                AopContext.setCurrentProxy(oldProxy);
            }
        }
    }
}

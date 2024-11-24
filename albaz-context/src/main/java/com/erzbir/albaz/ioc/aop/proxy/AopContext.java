package com.erzbir.albaz.ioc.aop.proxy;

/**
 * @author erzbir
 * @since 1.0.0
 */
public final class AopContext {

    public static final ThreadLocal<Object> currentProxy = new ThreadLocal<>();

    private AopContext() {
    }

    public static Object currentProxy() {
        Object proxy = currentProxy.get();
        if (proxy == null) {
            throw new IllegalStateException("当前没有代理在运行");
        }
        return proxy;
    }

    public static Object setCurrentProxy(Object proxy) {
        Object old = currentProxy.get();
        if (proxy != null) {
            currentProxy.set(proxy);
        } else {
            currentProxy.remove();
        }
        return old;
    }

}

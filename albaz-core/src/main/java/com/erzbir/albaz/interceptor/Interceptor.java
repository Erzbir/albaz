package com.erzbir.albaz.interceptor;

/**
 * 拦截器
 * <p></p>
 * 返回 {@code true} 则代表拦截
 *
 * @author Erzbir
 * @since 1.0.0
 */
public interface Interceptor<E> {

    boolean intercept(E target);
}

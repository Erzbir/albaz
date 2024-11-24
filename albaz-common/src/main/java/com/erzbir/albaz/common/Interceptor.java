package com.erzbir.albaz.common;

/**
 * <p>
 * 拦截器
 * </p>
 *
 * @author Erzbir
 * @since 1.0.0
 */
public interface Interceptor<E> {

    boolean intercept(E target);
}

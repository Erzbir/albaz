package com.erzbir.albaz.common;

/**
 * <p>
 * 拦截器, 返回 true 表示放行, false 表示拦截
 * </p>
 *
 * @author Erzbir
 * @since 1.0.0
 */
public interface Interceptor<E> {

    boolean intercept(E target);

    Class<E> getTargetClass();
}

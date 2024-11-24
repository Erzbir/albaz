package com.erzbir.albaz.dispatch;


import com.erzbir.albaz.common.Interceptor;

import java.util.List;

/**
 * <p>
 * 对一个目标执行拦截器的逻辑
 * </p>
 *
 * @author Erzbir
 * @since 1.0.0
 */
public interface InterceptProcessor {
    <E> boolean intercept(E target, List<Interceptor<E>> interceptors);
}
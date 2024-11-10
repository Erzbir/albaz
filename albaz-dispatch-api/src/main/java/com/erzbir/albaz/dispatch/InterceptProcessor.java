package com.erzbir.albaz.dispatch;


import com.erzbir.albaz.interceptor.Interceptor;

import java.util.List;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public interface InterceptProcessor {
    <E> boolean intercept(E target, List<Interceptor<E>> interceptors);
}
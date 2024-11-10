package com.erzbir.albaz.dispatch.internal;

import com.erzbir.albaz.dispatch.InterceptProcessor;
import com.erzbir.albaz.interceptor.Interceptor;

import java.util.List;

/**
 * @author Erzbir
 * @since 1.0.0
 */
class DefaultInterceptProcessor implements InterceptProcessor {
    public <E> boolean intercept(E target, List<Interceptor<E>> interceptors) {
        boolean flag = true;
        for (Interceptor<E> interceptor : interceptors) {
            flag = interceptor.intercept(target);
        }
        return flag;
    }
}
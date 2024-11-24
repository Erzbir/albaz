package com.erzbir.albaz.dispatch.internal;

import com.erzbir.albaz.common.Interceptor;
import com.erzbir.albaz.dispatch.InterceptProcessor;

import java.util.List;

/**
 * <p>
 * 当返回 {@code false} 时表示拦截
 * </p>
 *
 * @author Erzbir
 * @see InterceptProcessor
 * @see Interceptor
 * @since 1.0.0
 */
final class InternalInterceptProcessor implements InterceptProcessor {
    public <E> boolean intercept(E target, List<Interceptor<E>> interceptors) {
        boolean flag = true;
        for (Interceptor<E> interceptor : interceptors) {
            flag &= interceptor.intercept(target);
        }
        return flag;
    }
}
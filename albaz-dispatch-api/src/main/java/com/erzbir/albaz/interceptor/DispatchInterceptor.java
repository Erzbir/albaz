package com.erzbir.albaz.interceptor;


import com.erzbir.albaz.dispatch.EventContext;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public interface DispatchInterceptor extends Interceptor<EventContext> {
    @Override
    boolean intercept(EventContext target);
}

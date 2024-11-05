package com.erzbir.albaz.event.interceptor;


import com.erzbir.albaz.event.EventContext;
import com.erzbir.albaz.interceptor.Interceptor;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public interface DispatchInterceptor extends Interceptor<EventContext> {
    @Override
    boolean intercept(EventContext target);
}

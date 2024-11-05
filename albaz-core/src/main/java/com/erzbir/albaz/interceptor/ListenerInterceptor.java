package com.erzbir.albaz.interceptor;


import com.erzbir.albaz.event.ListenerContext;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public interface ListenerInterceptor extends Interceptor<ListenerContext> {
    @Override
    boolean intercept(ListenerContext target);
}

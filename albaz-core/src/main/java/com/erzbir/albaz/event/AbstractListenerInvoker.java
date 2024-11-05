package com.erzbir.albaz.event;


import com.erzbir.albaz.interceptor.Interceptor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public abstract class AbstractListenerInvoker implements ListenerInvoker {
    protected final List<Interceptor<ListenerContext>> interceptors = new ArrayList<>();

    @Override
    public void addInterceptor(Interceptor<ListenerContext> interceptor) {
        interceptors.add(interceptor);
    }
}

package com.erzbir.albaz.dispatch.channel;


import com.erzbir.albaz.common.Interceptor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public abstract class AbstractListenerInvoker implements ListenerInvoker {
    protected final List<Interceptor<InvokerContext>> interceptors = new ArrayList<>();

    @Override
    public void addInterceptor(Interceptor<InvokerContext> interceptor) {
        interceptors.add(interceptor);
    }
}

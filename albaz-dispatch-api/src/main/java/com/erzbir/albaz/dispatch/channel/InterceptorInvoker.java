package com.erzbir.albaz.dispatch.channel;

import com.erzbir.albaz.common.Interceptor;
import com.erzbir.albaz.dispatch.listener.ListenerStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public class InterceptorInvoker extends AbstractListenerInvoker implements ListenerInvoker {
    protected final List<Interceptor<InvokerContext>> interceptors = new ArrayList<>();

    public InterceptorInvoker() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public ListenerStatus invoke(InvokerContext invokerContext) {
        if (!intercept(invokerContext)) {
            return ListenerStatus.TRUNCATED;
        }
        return invokerContext.getListener().onEvent(invokerContext.getEvent());
    }


    private boolean intercept(InvokerContext invokerContext) {
        boolean flag = true;
        for (Interceptor<InvokerContext> interceptor : interceptors) {
            flag &= interceptor.intercept(invokerContext);
        }
        return flag;
    }
}

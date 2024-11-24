package com.erzbir.albaz.dispatch.channel;


import com.erzbir.albaz.common.Interceptor;
import com.erzbir.albaz.dispatch.event.Event;
import com.erzbir.albaz.dispatch.listener.Listener;
import com.erzbir.albaz.dispatch.listener.ListenerStatus;

/**
 * <p>
 * 监听执行器, 监听器并不直接执行监听回调, 而是通过这个接口来执行
 * </p>
 *
 * @author Erzbir
 * @since 1.0.0
 */
public interface ListenerInvoker {
    ListenerStatus invoke(InvokerContext invokerContext);

    void addInterceptor(Interceptor<InvokerContext> interceptor);

    @SuppressWarnings("rawtypes")
    record InvokerContext(Event event, Listener listener) {
        public Event getEvent() {
            return event;
        }

        public Listener getListener() {
            return listener;
        }
    }
}


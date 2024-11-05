package com.erzbir.albaz.event;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public interface ListenerInvokers {
    @Slf4j
    class BaseListenerInvoker extends AbstractListenerInvoker implements ListenerInvoker {

        @SuppressWarnings("unchecked")
        @Override
        public ListenerResult invoke(ListenerContext listenerContext) {
            Event event = listenerContext.getEvent();
            return listenerContext.getListener().onEvent(event);
        }
    }

    @Slf4j
    class InterceptorInvoker extends AbstractListenerInvoker implements ListenerInvoker {
        private final ListenerInvoker listenerInvoker;
        private final InterceptProcessor interceptProcessor;

        public InterceptorInvoker() {
            this.listenerInvoker = new BaseListenerInvoker();
            this.interceptProcessor = new DefaultInterceptProcessor();
        }

        @Override
        public ListenerResult invoke(ListenerContext listenerContext) {
            if (!intercept(listenerContext)) {
                return StandardListenerResult.TRUNCATED;
            }
            return listenerInvoker.invoke(listenerContext);
        }


        private boolean intercept(ListenerContext listenerContext) {
            return interceptProcessor.intercept(listenerContext, interceptors);
        }

    }

}

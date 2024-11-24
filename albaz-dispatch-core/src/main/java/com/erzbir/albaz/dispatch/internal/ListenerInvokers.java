package com.erzbir.albaz.dispatch.internal;

import com.erzbir.albaz.dispatch.*;
import com.erzbir.albaz.logging.Log;
import com.erzbir.albaz.logging.LogFactory;

/**
 * <p>
 * 一些执行器的实现
 * </p>
 *
 * @author Erzbir
 * @since 1.0.0
 */
interface ListenerInvokers {
    class BaseListenerInvoker extends AbstractListenerInvoker implements ListenerInvoker {
        private final Log log = LogFactory.getLog(getClass());

        @SuppressWarnings({"unchecked", "rawtypes"})
        @Override
        public ListenerStatus invoke(InvokerContext invokerContext) {
            Event event = invokerContext.getEvent();
            Listener listener = invokerContext.getListener();
            log.debug("Invoke event: " + event + " callback: " + listener.getClass().getName());
            return listener.onEvent(event);
        }
    }

    /**
     * <p>带有拦截功能的执行器, 拦截后不执行监听回调, 并返回 {@link  ListenerStatus}</p>
     *
     * @see ListenerStatus
     * @see InterceptProcessor
     */
    class InterceptorInvoker extends AbstractListenerInvoker implements ListenerInvoker {
        private final ListenerInvoker listenerInvoker;
        private final InterceptProcessor interceptProcessor;

        public InterceptorInvoker() {
            this.listenerInvoker = new BaseListenerInvoker();
            this.interceptProcessor = new InternalInterceptProcessor();
        }

        @Override
        public ListenerStatus invoke(InvokerContext invokerContext) {
            if (!intercept(invokerContext)) {
                return ListenerStatus.TRUNCATED;
            }
            return listenerInvoker.invoke(invokerContext);
        }


        private boolean intercept(InvokerContext invokerContext) {
            return interceptProcessor.intercept(invokerContext, interceptors);
        }

    }

}

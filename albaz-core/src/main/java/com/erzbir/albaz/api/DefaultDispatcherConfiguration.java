package com.erzbir.albaz.api;

import com.erzbir.albaz.event.ListenerInvoker;
import com.erzbir.albaz.event.ListenerInvokers;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public class DefaultDispatcherConfiguration implements DispatcherConfiguration {
    private Mode mode = Mode.POLL;
    private ListenerInvoker invoker = new ListenerInvokers.InterceptorInvoker();

    @Override
    public ListenerInvoker getInvoker() {
        return invoker;
    }

    @Override
    public void setInvoker(ListenerInvoker invoker) {
        this.invoker = invoker;
    }

    @Override
    public Mode getMode() {
        return mode;
    }

    @Override
    public void setMode(Mode mode) {
        this.mode = mode;
    }
}

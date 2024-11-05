package com.erzbir.albaz.api;


import com.erzbir.albaz.event.ListenerInvoker;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public interface DispatcherConfiguration {
    ListenerInvoker getInvoker();

    void setInvoker(ListenerInvoker invoker);

    Mode getMode();

    void setMode(Mode mode);

    enum Mode {
        POLL,
        NOTIFY
    }
}

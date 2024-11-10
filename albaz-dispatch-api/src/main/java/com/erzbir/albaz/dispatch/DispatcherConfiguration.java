package com.erzbir.albaz.dispatch;


/**
 * @author Erzbir
 * @since 1.0.0
 */
public interface DispatcherConfiguration {
    Mode getMode();

    void setMode(Mode mode);

    enum Mode {
        POLL,
        NOTIFY
    }
}

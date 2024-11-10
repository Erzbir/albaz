package com.erzbir.albaz.dispatch.internal;


import com.erzbir.albaz.dispatch.DispatcherConfiguration;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public class DefaultDispatcherConfiguration implements DispatcherConfiguration {
    private Mode mode = Mode.POLL;

    @Override
    public Mode getMode() {
        return mode;
    }

    @Override
    public void setMode(Mode mode) {
        this.mode = mode;
    }
}

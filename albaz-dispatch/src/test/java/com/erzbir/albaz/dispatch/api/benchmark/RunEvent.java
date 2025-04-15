package com.erzbir.albaz.dispatch.api.benchmark;

import com.erzbir.albaz.dispatch.event.AbstractEvent;
import com.erzbir.albaz.dispatch.event.Event;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public class RunEvent extends AbstractEvent implements Event {
    public RunEvent(Object source) {
        super(source);
    }

    @Override
    public Object getSource() {
        return source;
    }

    public String getName() {
        return "Event Test";
    }
}

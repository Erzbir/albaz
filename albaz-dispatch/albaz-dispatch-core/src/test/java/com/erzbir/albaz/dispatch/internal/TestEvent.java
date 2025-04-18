package com.erzbir.albaz.dispatch.internal;

import com.erzbir.albaz.dispatch.event.AbstractEvent;
import com.erzbir.albaz.dispatch.event.Event;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public class TestEvent extends AbstractEvent implements Event {
    public TestEvent(Object source) {
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

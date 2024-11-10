package com.erzbir.albaz.dispatch.internal;

import com.erzbir.albaz.dispatch.AbstractEvent;
import com.erzbir.albaz.dispatch.Event;

/**
 * @author Erzbir
 * @Data: 2024/2/14 02:19
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

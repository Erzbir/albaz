package com.erzbir.albaz.dispatch.api.benchmark;


import com.erzbir.albaz.dispatch.event.AbstractEvent;
import com.erzbir.albaz.dispatch.event.Event;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public interface NamedEvent extends Event {
    String getName();
}

class TestNamedEvent extends AbstractEvent implements NamedEvent {
    private final String name;

    public TestNamedEvent(Object source, String name) {
        super(source);
        this.name = name;
    }

    @Override
    public Object getSource() {
        return source;
    }

    @Override
    public String getName() {
        return name;
    }
}

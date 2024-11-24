package com.erzbir.albaz.dispatch.api;


import com.erzbir.albaz.dispatch.event.AbstractEvent;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public class TestBotEvent extends AbstractEvent implements BotEvent {
    public TestBotEvent(Object source) {
        super(source);
    }

    @Override
    public Object getSource() {
        return source;
    }

    public String getName() {
        return "bot: test";
    }

    @Override
    public String getBot() {
        return "TestBot";
    }
}

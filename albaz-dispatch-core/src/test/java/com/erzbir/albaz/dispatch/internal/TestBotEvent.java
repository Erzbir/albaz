package com.erzbir.albaz.dispatch.internal;


import com.erzbir.albaz.dispatch.AbstractEvent;

/**
 * @author Erzbir
 * @Data: 2024/2/14 02:19
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

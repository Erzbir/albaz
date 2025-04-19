package com.erzbir.albaz.dispatch.usage.imchat;

import com.erzbir.albaz.dispatch.event.AbstractEvent;
import com.erzbir.albaz.dispatch.event.Event;

import static com.erzbir.albaz.dispatch.usage.PrintConstants.GREEN;
import static com.erzbir.albaz.dispatch.usage.PrintConstants.RESET;

/**
 * @author Erzbir
 * @since 1.0.0
 */

public interface BotEvent extends Event {
    String getBotName();
}

interface MessageEvent extends BotEvent {
    Message getMessage();
}

class GroupMessageEvent extends AbstractEvent implements MessageEvent {
    GroupMessage message;
    String bot;

    public GroupMessageEvent(Object source, GroupMessage groupMessage, String bot) {
        super(source);
        this.message = groupMessage;
        this.bot = bot;
    }

    @Override
    public GroupMessage getMessage() {
        return message;
    }

    @Override
    public Object getSource() {
        return source;
    }

    @Override
    public String getBotName() {
        return bot;
    }
}

class UserMessageEvent extends AbstractEvent implements MessageEvent {
    UserMessage message;
    String bot;

    public UserMessageEvent(Object source, UserMessage userMessage, String bot) {
        super(source);
        this.message = userMessage;
        this.bot = bot;
    }

    @Override
    public UserMessage getMessage() {
        return message;
    }

    @Override
    public String getBotName() {
        return bot;
    }

    public void reply(String msg) {
        System.out.println(GREEN + "reply message " + " to user " + message.user + ": " + msg + RESET);
    }

    @Override
    public Object getSource() {
        return source;
    }
}
package com.erzbir.albaz.dispatch.usage.imchat;

import java.io.Serializable;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public interface Message extends Serializable {
    MessageProto toProto();
}

class UserMessage implements Message {
    public final String user;
    public final String message;

    public UserMessage(String user, String message) {
        this.user = user;
        this.message = message;
    }

    @Override
    public MessageProto toProto() {
        return new MessageProto(Protocol.USER_MESSAGE_CODE, user, message);
    }
}

class GroupMessage implements Message {
    public String group;
    public String message;

    public GroupMessage(String group, String message) {
        this.group = group;
        this.message = message;
    }

    @Override
    public MessageProto toProto() {
        return new MessageProto(Protocol.GROUP_MESSAGE_CODE, group, message);
    }
}
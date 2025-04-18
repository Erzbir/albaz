package com.erzbir.albaz.dispatch.api.usage.imchat;

import java.io.*;
import java.nio.charset.StandardCharsets;

public interface Protocol extends Externalizable {
    byte USER_MESSAGE_CODE = 0x01;
    byte GROUP_MESSAGE_CODE = 0x02;

    byte[] serialize() throws IOException;
}

/**
 * A simple message protocol
 */
class MessageProto implements Protocol {
    public byte code;
    public byte s_len;
    public byte[] subject;
    public byte msg_len;
    public byte[] message;

    public MessageProto() {
    }

    public MessageProto(byte code, String subject, String message) {
        this.code = code;
        byte[] subjectBytes = subject.getBytes(StandardCharsets.UTF_8);
        this.s_len = (byte) subjectBytes.length;
        this.subject = subjectBytes;
        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
        this.msg_len = (byte) messageBytes.length;
        this.message = messageBytes;
    }

    public static MessageProto readFromIO(InputStream input) throws IOException {
        // Read message type
        byte code = input.readNBytes(1)[0];

        // Read subject length and content
        byte subjectLen = input.readNBytes(1)[0];

        byte[] subjectBuffer = new byte[subjectLen];
        input.read(subjectBuffer);

        // Read message length and content
        int messageLen = input.readNBytes(1)[0];

        byte[] messageBuffer = new byte[messageLen];
        input.read(messageBuffer);

        String subject = new String(subjectBuffer, StandardCharsets.UTF_8);
        String message = new String(messageBuffer, StandardCharsets.UTF_8);


        return new MessageProto(code, subject, message);
    }

    public byte[] serialize() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(code);
        out.write(s_len);
        out.write(subject);
        out.write(msg_len);
        out.write(message);
        return out.toByteArray();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.write(serialize());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException {

        byte sLen = in.readByte();
        this.s_len = sLen;

        byte[] subjectBuffer = new byte[sLen];
        in.read(subjectBuffer);
        this.subject = subjectBuffer;

        byte mLen = in.readByte();
        this.msg_len = mLen;

        byte[] msgBuffer = new byte[mLen];
        in.read(msgBuffer);
        this.message = msgBuffer;
    }
}

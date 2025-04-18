package com.erzbir.albaz.dispatch.api.usage.imchat;

import java.io.Closeable;
import java.io.IOException;
import java.io.PipedOutputStream;

public class IMServer implements Closeable {
    // Simulate network connection
    private final PipedOutputStream output;

    public IMServer(PipedOutputStream output) {
        this.output = output;
    }

    public void sendUserMessage(UserMessage userMessage) throws IOException {
        synchronized (output) {
            MessageProto proto = userMessage.toProto();
            byte[] bytes = proto.serialize();
            output.write(bytes);
            output.flush();
        }
    }

    public void sendGroupMessage(GroupMessage groupMessage) throws IOException {
        synchronized (output) {
            MessageProto proto = groupMessage.toProto();
            byte[] bytes = proto.serialize();
            output.write(bytes);
            output.flush();
        }
    }

    @Override
    public void close() throws IOException {
        output.close();
    }
}
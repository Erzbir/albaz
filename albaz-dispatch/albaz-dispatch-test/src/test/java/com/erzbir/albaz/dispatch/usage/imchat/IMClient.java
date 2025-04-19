package com.erzbir.albaz.dispatch.usage.imchat;

import com.erzbir.albaz.dispatch.EventDispatcher;

import java.io.Closeable;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class IMClient implements Closeable {
    private final String bot;
    private final EventDispatcher eventDispatcher;
    // Simulate network connection
    private final PipedInputStream input;
    private volatile boolean running = true;
    private Thread receiveThread;

    public IMClient(EventDispatcher eventDispatcher, String bot, PipedOutputStream output) throws IOException {
        this.eventDispatcher = eventDispatcher;
        this.bot = bot;
        this.input = new PipedInputStream(output);
    }

    public void receive() {
        receiveThread = Thread.ofVirtual().name("IRC-Client-Receiver").start(() -> {
            try {
                while (running && !Thread.currentThread().isInterrupted()) {
                    if (input.available() <= 0) {
                        // Avoid busy waiting
                        TimeUnit.MILLISECONDS.sleep(200);
                        continue;
                    }

                    MessageProto messageProto = MessageProto.readFromIO(input);

                    switch (messageProto.code) {
                        case Protocol.USER_MESSAGE_CODE ->
                                receivedUserMessage(new UserMessage(new String(messageProto.subject, StandardCharsets.UTF_8), new String(messageProto.message, StandardCharsets.UTF_8)));
                        case Protocol.GROUP_MESSAGE_CODE ->
                                receivedGroupMessage(new GroupMessage(new String(messageProto.subject, StandardCharsets.UTF_8), new String(messageProto.message, StandardCharsets.UTF_8)));
                        default -> System.err.println("Unknown message code: " + messageProto.code);
                    }
                }
            } catch (IOException e) {
                if (running) {
                    System.err.println("IO error while receiving: " + e.getMessage());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Receive thread interrupted");
            }
        });
    }

    public void receivedUserMessage(UserMessage message) {
        UserMessageEvent userMessageEvent = new UserMessageEvent(this, message, bot);
        eventDispatcher.dispatch(userMessageEvent);
    }

    public void receivedGroupMessage(GroupMessage message) {
        GroupMessageEvent groupMessageEvent = new GroupMessageEvent(this, message, bot);
        eventDispatcher.dispatch(groupMessageEvent);
    }


    @Override
    public void close() throws IOException {
        running = false;
        if (receiveThread != null) {
            receiveThread.interrupt();
        }
        input.close();
    }
}
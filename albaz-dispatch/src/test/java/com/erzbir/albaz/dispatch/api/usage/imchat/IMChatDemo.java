package com.erzbir.albaz.dispatch.api.usage.imchat;

import com.erzbir.albaz.dispatch.EventDispatcher;
import com.erzbir.albaz.dispatch.impl.GlobalEventChannel;
import com.erzbir.albaz.dispatch.internal.NotificationEventDispatcher;
import com.erzbir.albaz.dispatch.internal.PollingEventDispatcher;
import com.erzbir.albaz.dispatch.listener.Listener;
import com.erzbir.albaz.dispatch.listener.ListenerStatus;

import java.io.IOException;
import java.io.PipedOutputStream;

import static com.erzbir.albaz.dispatch.api.usage.PrintConstants.BLUE;
import static com.erzbir.albaz.dispatch.api.usage.PrintConstants.RESET;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public class IMChatDemo {
    public void demoRun(EventDispatcher eventDispatcher) throws IOException {
        eventDispatcher.start();

        String bot = "Albaz";

        PipedOutputStream output = new PipedOutputStream();
        IMClient IMClient = new IMClient(eventDispatcher, bot, output);
        IMServer IMServer = new IMServer(output);

        // Register the listener
        Listener<UserMessageEvent> userMessageEventListener = new Listener<>() {
            @Override
            public ListenerStatus onEvent(UserMessageEvent event) {
                UserMessage message = event.getMessage();
                System.out.println(BLUE + "Received user message: " + message.message + " from user: " + message.user + RESET);
                event.reply("Hi " + message.user + " I've received your message");
                // Keep listening
                return ListenerStatus.CONTINUE;
            }
        };

        // Register with global channel
        GlobalEventChannel globalEventChannel = GlobalEventChannel.INSTANCE;
        globalEventChannel.registerListener(UserMessageEvent.class, userMessageEventListener);

        // Register group message listener
        globalEventChannel.registerListener(GroupMessageEvent.class, new Listener<>() {
            @Override
            public ConcurrencyKind concurrencyKind() {
                // synchronized mode (when remove listener)
                return ConcurrencyKind.LOCKED;
            }

            @Override
            public ListenerStatus onEvent(GroupMessageEvent event) {
                GroupMessage message = event.getMessage();
                System.out.println(BLUE + "Received group message: " + message.message + " in group: " + message.group + RESET);
                // listen only once
                return ListenerStatus.STOP;
            }
        });

        // Client starts receiving
        IMClient.receive();

        // Server sends messages
        IMServer.sendUserMessage(new UserMessage("Albaz", "Hi I am Albaz"));
        IMServer.sendGroupMessage(new GroupMessage("GroupA", "This group is great"));
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        IMChatDemo IMChatDemo = new IMChatDemo();
        EventDispatcher eventDispatcher = new PollingEventDispatcher();

        try {
            IMChatDemo.demoRun(eventDispatcher);

            eventDispatcher.await();
            Thread.sleep(3000);

//            eventDispatcher.cancel();
        } catch (Exception e) {
            System.err.println("Demo execution error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
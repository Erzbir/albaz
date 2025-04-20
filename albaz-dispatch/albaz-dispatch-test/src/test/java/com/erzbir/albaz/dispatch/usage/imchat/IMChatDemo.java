package com.erzbir.albaz.dispatch.usage.imchat;

import com.erzbir.albaz.common.Interceptor;
import com.erzbir.albaz.dispatch.AsyncEventDispatcher;
import com.erzbir.albaz.dispatch.channel.EventChannel;
import com.erzbir.albaz.dispatch.event.Event;
import com.erzbir.albaz.dispatch.listener.Listener;
import com.erzbir.albaz.dispatch.listener.ListenerStatus;
import com.erzbir.albaz.dispatch.spi.EventDispatcherProvider;

import java.io.IOException;
import java.io.PipedOutputStream;

import static com.erzbir.albaz.dispatch.usage.PrintConstants.BLUE;
import static com.erzbir.albaz.dispatch.usage.PrintConstants.RESET;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public class IMChatDemo {
    public static void main(String[] args) throws Exception {
        IMChatDemo IMChatDemo = new IMChatDemo();
        AsyncEventDispatcher eventDispatcher = AsyncEventDispatcher.of(EventDispatcherProvider.INSTANCE.getInstance("com.erzbir.albaz.dispatch.internal.NotificationEventDispatcher"));
        eventDispatcher = eventDispatcher.async();
        try {
            IMChatDemo.demoRun(eventDispatcher);

            eventDispatcher.await();
            Thread.sleep(3000);

            eventDispatcher.close();
        } catch (Exception e) {
            System.err.println("Demo execution error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void demoRun(AsyncEventDispatcher eventDispatcher) throws IOException {
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
        EventChannel<Event> eventChannel = eventDispatcher.getEventChannel();
        eventChannel.registerListener(UserMessageEvent.class, userMessageEventListener);

        // Register group message listener
        eventChannel.registerListener(GroupMessageEvent.class, new Listener<>() {
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
                return ListenerStatus.CONTINUE;
            }
        });

        eventDispatcher.addInterceptor(new KeywordsInterceptor());

        // Client starts receiving
        IMClient.receive();

        // Server sends messages
        IMServer.sendUserMessage(new UserMessage("Albaz", "Hi I am Albaz"));
        IMServer.sendGroupMessage(new GroupMessage("GroupA", "This group is great"));
        IMServer.sendGroupMessage(new GroupMessage("GroupA", "Hi fxxk you"));
    }

    static class KeywordsInterceptor implements Interceptor<GroupMessageEvent> {
        @Override
        public boolean intercept(GroupMessageEvent target) {
            if (target instanceof GroupMessageEvent groupMessageEvent) {
                return !groupMessageEvent.message.message.contains("fxxk");
            }
            return true;
        }

        @Override
        public Class<GroupMessageEvent> getTargetClass() {
            return GroupMessageEvent.class;
        }
    }
}
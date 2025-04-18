package com.erzbir.albaz.dispatch.channel;

import com.erzbir.albaz.common.Interceptor;
import com.erzbir.albaz.dispatch.event.Event;
import com.erzbir.albaz.dispatch.listener.Listener;
import com.erzbir.albaz.dispatch.listener.ListenerHandle;
import com.erzbir.albaz.dispatch.listener.ListenerStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

class EventChannelTest {
    class JunitEventChannel extends EventChannel<Event> {

        public JunitEventChannel(Class<Event> baseEventClass) {
            super(baseEventClass);
        }

        @Override
        public void broadcast(Event event) {
            throw new UnsupportedOperationException();
        }

        @Override
        public <T extends Event> ListenerHandle registerListener(Class<T> eventType, Listener<T> listener) {
            throw new UnsupportedOperationException();
        }

        @Override
        public <T extends Event> ListenerHandle subscribe(Class<T> eventType, Function<T, ListenerStatus> handle) {
            throw new UnsupportedOperationException();
        }

        @Override
        public <T extends Event> ListenerHandle subscribeOnce(Class<T> eventType, Consumer<T> handle) {
            throw new UnsupportedOperationException();
        }

        @Override
        public <T extends Event> ListenerHandle subscribeAlways(Class<T> eventType, Consumer<T> handle) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Listener<Event> createListener(Function<Event, ListenerStatus> handle) {
            throw new UnsupportedOperationException();
        }

        @Override
        public EventChannel<Event> filter(Predicate<? extends Event> predicate) {
            throw new UnsupportedOperationException();
        }

        @Override
        public <T extends Event> EventChannel<T> filterInstance(Class<T> eventType) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Iterable<Listener<Event>> getListeners() {
            throw new UnsupportedOperationException();
        }
    }

    @Test
    void getBaseEventClass() {
        JunitEventChannel channel = new JunitEventChannel(Event.class);
        Assertions.assertEquals(Event.class, channel.getBaseEventClass());
    }

    @Test
    void addInterceptor() {
        JunitEventChannel channel = new JunitEventChannel(Event.class);
        Assertions.assertTrue(channel.interceptors.isEmpty());
        channel.addInterceptor(new Interceptor() {
            @Override
            public boolean intercept(Object target) {
                return false;
            }
        });
        Assertions.assertNotNull(channel.interceptors.getFirst());
    }

    @Test
    void close() {
        JunitEventChannel channel = new JunitEventChannel(Event.class);
        Assertions.assertTrue(channel.activated.get());
        channel.close();
        Assertions.assertFalse(channel.activated.get());
    }

    @Test
    void open() {
        JunitEventChannel channel = new JunitEventChannel(Event.class);
        Assertions.assertTrue(channel.activated.get());
        channel.cancel();
        channel.open();
        Assertions.assertTrue(channel.activated.get());
    }

    @Test
    void cancel() {
        JunitEventChannel channel = new JunitEventChannel(Event.class);
        Assertions.assertTrue(channel.activated.get());
        channel.cancel();
        Assertions.assertFalse(channel.activated.get());
    }

    @Test
    void isCanceled() {
        JunitEventChannel channel = new JunitEventChannel(Event.class);
        Assertions.assertTrue(channel.activated.get());
        channel.cancel();
        channel.open();
        Assertions.assertFalse(channel.isCanceled());
        Assertions.assertTrue(channel.activated.get());
    }
}
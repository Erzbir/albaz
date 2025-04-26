package com.erzbir.albaz.dispatch.internal;

import com.erzbir.albaz.dispatch.channel.EventChannel;
import com.erzbir.albaz.dispatch.event.Event;
import com.erzbir.albaz.dispatch.listener.Listener;
import com.erzbir.albaz.dispatch.listener.ListenerStatus;
import com.erzbir.albaz.logging.Log;
import com.erzbir.albaz.logging.LogFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

class EventChannelImplTest {
    Log log = LogFactory.getLog(EventChannelImplTest.class);

    @Test
    void close() {
        EventChannelImpl<Event> eventChannel = new EventChannelImpl<>(Event.class);
        eventChannel.close();
        Assertions.assertTrue(eventChannel.isClosed());
    }

    @Test
    void open() {
        EventChannelImpl<Event> eventChannel = new EventChannelImpl<>(Event.class);
        eventChannel.close();
        eventChannel.open();
        Assertions.assertFalse(eventChannel.isClosed());
    }

    @Test
    void isClosed() {
        EventChannelImpl<Event> eventChannel = new EventChannelImpl<>(Event.class);
        eventChannel.close();
        Assertions.assertTrue(eventChannel.isClosed());
    }

    @Test
    void broadcast() {
        EventChannelImpl<Event> eventChannel = new EventChannelImpl<>(Event.class);
        eventChannel.broadcast(new TestEvent(this));
    }

    @Test
    void registerListener() throws InterruptedException {
        EventChannelImpl<Event> eventChannel = new EventChannelImpl<>(Event.class);
        AtomicBoolean flag = new AtomicBoolean(false);
        eventChannel.registerListener(TestEvent.class, event -> {
            flag.set(true);
            return ListenerStatus.CONTINUE;
        });
        eventChannel.broadcast(new TestEvent(this));
        Thread.sleep(100);
        Assertions.assertTrue(flag.get());
    }

    @Test
    void subscribe() throws InterruptedException {
        EventChannelImpl<Event> eventChannel = new EventChannelImpl<>(Event.class);
        AtomicBoolean flag = new AtomicBoolean(false);
        eventChannel.subscribe(Event.class, event -> {
            flag.set(true);
            return ListenerStatus.CONTINUE;
        });
        eventChannel.broadcast(new TestEvent(this));
        Thread.sleep(100);
        Assertions.assertTrue(flag.get());
    }

    @Test
    void subscribeOnce() throws InterruptedException {
        EventChannelImpl<Event> eventChannel = new EventChannelImpl<>(Event.class);
        AtomicBoolean flag = new AtomicBoolean(false);
        eventChannel.subscribeOnce(Event.class, event -> flag.set(true));
        eventChannel.broadcast(new TestEvent(this));
        Thread.sleep(100);
        Assertions.assertTrue(flag.get());
    }

    @Test
    void subscribeAlways() throws InterruptedException {
        EventChannelImpl<Event> eventChannel = new EventChannelImpl<>(Event.class);
        AtomicBoolean flag = new AtomicBoolean(false);
        eventChannel.subscribeAlways(Event.class, event -> flag.set(true));
        eventChannel.broadcast(new TestEvent(this));
        Thread.sleep(100);
        Assertions.assertTrue(flag.get());
        flag.set(false);
        eventChannel.broadcast(new TestEvent(this));
        Thread.sleep(100);
        Assertions.assertTrue(flag.get());
    }

    @Test
    void filter() throws InterruptedException {
        EventChannelImpl<Event> eventChannel = new EventChannelImpl<>(Event.class);
        AtomicBoolean flag = new AtomicBoolean(false);
        AtomicBoolean flag2 = new AtomicBoolean(false);
        EventChannel<Event> testEventEventChannel1 = eventChannel.filter(event -> {
            flag.set(true);
            return true;
        });
        testEventEventChannel1.subscribeAlways(TestEvent.class, event -> {
            flag2.set(true);
        });
        eventChannel.broadcast(new TestNamedEvent(this, "test"));
        eventChannel.broadcast(new TestEvent(this));
        Thread.sleep(100);
        Assertions.assertTrue(flag.get());
        Assertions.assertTrue(flag2.get());


    }

    @Test
    void filterInstance() throws InterruptedException {
        EventChannelImpl<Event> eventChannel = new EventChannelImpl<>(Event.class);
        AtomicBoolean flag = new AtomicBoolean(false);
        EventChannel<NamedEvent> testEventEventChannel = eventChannel.filterInstance(NamedEvent.class);
        EventChannel<TestNamedEvent> testEventEventChannel1 = testEventEventChannel.filterInstance(TestNamedEvent.class);
        testEventEventChannel1.registerListener(TestNamedEvent.class, new Listener<>() {
            @Override
            public ListenerStatus onEvent(TestNamedEvent event) {
                flag.set(true);
                return ListenerStatus.CONTINUE;
            }
        });
        eventChannel.broadcast(new TestNamedEvent(this, "sass"));
        Thread.sleep(100);
        Assertions.assertTrue(flag.get());
    }
}
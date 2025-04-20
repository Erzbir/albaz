package com.erzbir.albaz.dispatch.internal;

import com.erzbir.albaz.dispatch.channel.EventChannel;
import com.erzbir.albaz.dispatch.event.Event;
import com.erzbir.albaz.dispatch.listener.Listener;
import com.erzbir.albaz.dispatch.listener.ListenerStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

class EventChannelImplTest {

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
    void registerListener() {
        EventChannelImpl<Event> eventChannel = new EventChannelImpl<>(Event.class);
        eventChannel.registerListener(TestEvent.class, event -> {
            Assertions.assertEquals(TestEvent.class, event.getClass());
            return ListenerStatus.CONTINUE;
        });
        Assertions.assertNotNull(eventChannel.getListeners().iterator().next());
    }

    @Test
    void subscribe() {
        EventChannelImpl<Event> eventChannel = new EventChannelImpl<>(Event.class);
        eventChannel.subscribe(TestEvent.class, event -> {
            Assertions.assertEquals(TestEvent.class, event.getClass());
            return ListenerStatus.CONTINUE;
        });
        Assertions.assertNotNull(eventChannel.getListeners().iterator().next());
        Assertions.assertEquals(ListenerStatus.CONTINUE, eventChannel.getListeners().iterator().next().onEvent(new TestEvent(this)));
    }

    @Test
    void subscribeOnce() {
        EventChannelImpl<Event> eventChannel = new EventChannelImpl<>(Event.class);
        eventChannel.subscribeOnce(TestEvent.class, event -> Assertions.assertEquals(TestEvent.class, event.getClass()));
        Assertions.assertNotNull(eventChannel.getListeners().iterator().next());
        Assertions.assertEquals(ListenerStatus.STOP, eventChannel.getListeners().iterator().next().onEvent(new TestEvent(this)));
    }

    @Test
    void subscribeAlways() {
        EventChannelImpl<Event> eventChannel = new EventChannelImpl<>(Event.class);
        eventChannel.subscribeAlways(TestEvent.class, event -> Assertions.assertEquals(TestEvent.class, event.getClass()));
        Assertions.assertNotNull(eventChannel.getListeners().iterator().next());
        Assertions.assertEquals(ListenerStatus.CONTINUE, eventChannel.getListeners().iterator().next().onEvent(new TestEvent(this)));
    }

    @Test
    void filter() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        EventChannelImpl<Event> eventChannel = new EventChannelImpl<>(Event.class);
        EventChannel<TestEvent> testEventEventChannel = eventChannel.filterInstance(TestEvent.class);
        EventChannel<Event> testEventEventChannel1 = eventChannel.filter(event -> {
            Assertions.assertEquals(TestEvent.class, event.getClass());
            return true;
        });
        testEventEventChannel.registerListener(TestEvent.class, new Listener<TestEvent>() {
            @Override
            public ListenerStatus onEvent(TestEvent event) {
                Assertions.assertEquals(TestEvent.class, event.getClass());
                System.err.println(event);
                return ListenerStatus.STOP;
            }
        });
        eventChannel.registerListener(TestNamedEvent.class, event -> {
            System.err.println(event);
            return ListenerStatus.CONTINUE;
        });
//        testEventEventChannel.close();
        eventChannel.broadcast(new TestNamedEvent(this, "test"));
        eventChannel.broadcast(new TestEvent(this));
        Assertions.assertEquals(TestEvent.class, testEventEventChannel.getBaseEventClass());
        Assertions.assertEquals(Event.class, testEventEventChannel1.getBaseEventClass());
    }

    @Test
    void filterInstance() {
        EventChannelImpl<Event> eventChannel = new EventChannelImpl<>(Event.class);
        EventChannel<NamedEvent> testEventEventChannel = eventChannel.filterInstance(NamedEvent.class);
        EventChannel<TestNamedEvent> testEventEventChannel1 = testEventEventChannel.filterInstance(TestNamedEvent.class);
        Assertions.assertEquals(NamedEvent.class, testEventEventChannel.getBaseEventClass());
        Assertions.assertEquals(TestNamedEvent.class, testEventEventChannel1.getBaseEventClass());
    }

    @Test
    void getListeners() {
        EventChannelImpl<Event> eventChannel = new EventChannelImpl<>(Event.class);
        eventChannel.subscribeOnce(TestEvent.class, event -> Assertions.assertEquals(TestEvent.class, event.getClass()));
        Assertions.assertNotNull(eventChannel.getListeners().iterator().next());
    }
}
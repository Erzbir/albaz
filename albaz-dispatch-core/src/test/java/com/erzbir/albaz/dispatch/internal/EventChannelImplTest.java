package com.erzbir.albaz.dispatch.internal;

import com.erzbir.albaz.dispatch.Event;
import com.erzbir.albaz.dispatch.EventChannel;
import com.erzbir.albaz.dispatch.Listener;
import com.erzbir.albaz.dispatch.ListenerStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EventChannelImplTest {

    @Test
    void addInterceptor() {
        EventChannelImpl<Event> eventChannel = new EventChannelImpl<>(Event.class);
        eventChannel.addInterceptor(target -> false);
    }

    @Test
    void close() {
        EventChannelImpl<Event> eventChannel = new EventChannelImpl<>(Event.class);
        eventChannel.close();
        Assertions.assertTrue(eventChannel.isCanceled());
    }

    @Test
    void open() {
        EventChannelImpl<Event> eventChannel = new EventChannelImpl<>(Event.class);
        eventChannel.close();
        eventChannel.open();
        Assertions.assertFalse(eventChannel.isCanceled());

    }

    @Test
    void cancel() {
        EventChannelImpl<Event> eventChannel = new EventChannelImpl<>(Event.class);
        eventChannel.cancel();
        Assertions.assertTrue(eventChannel.isCanceled());
    }

    @Test
    void isCanceled() {
        EventChannelImpl<Event> eventChannel = new EventChannelImpl<>(Event.class);
        eventChannel.close();
        Assertions.assertTrue(eventChannel.isCanceled());
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
        eventChannel.subscribeOnce(TestEvent.class, event -> Assertions.assertEquals(TestEvent.class, event.getClass()));
        Assertions.assertNotNull(eventChannel.getListeners().iterator().next());
        Assertions.assertEquals(ListenerStatus.CONTINUE, eventChannel.getListeners().iterator().next().onEvent(new TestEvent(this)));
    }

    @Test
    void createListener() {
        EventChannelImpl<TestEvent> eventChannel = new EventChannelImpl<>(TestEvent.class);
        Listener<TestEvent> listener = eventChannel.createListener(event -> ListenerStatus.CONTINUE);
        Assertions.assertNotNull(listener);
        Assertions.assertEquals(EventChannelImpl.SafeListener.class, listener.getClass());
    }

    @Test
    void filter() {
        EventChannelImpl<Event> eventChannel = new EventChannelImpl<>(Event.class);
        EventChannel<TestEvent> testEventEventChannel = eventChannel.filterInstance(TestEvent.class);
        EventChannel<TestEvent> testEventEventChannel1 = testEventEventChannel.filter(event -> {
            Assertions.assertEquals(TestEvent.class, event.getClass());
            return true;
        });
        Assertions.assertEquals(TestEvent.class, testEventEventChannel.getBaseEventClass());
        Assertions.assertEquals(TestEvent.class, testEventEventChannel1.getBaseEventClass());
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
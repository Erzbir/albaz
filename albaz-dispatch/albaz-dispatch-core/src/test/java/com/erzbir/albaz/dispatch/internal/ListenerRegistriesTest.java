package com.erzbir.albaz.dispatch.internal;

import com.erzbir.albaz.dispatch.listener.Listener;
import com.erzbir.albaz.dispatch.listener.ListenerStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ListenerRegistriesTest {
    class JunitTestListener implements Listener<TestEvent> {
        @Override
        public ListenerStatus onEvent(TestEvent event) {
            Assertions.assertNotNull(event);
            return ListenerStatus.STOP;
        }
    }

    @Test
    void addListener() {
        ListenerRegistries registries = new ListenerRegistries();
        Assertions.assertTrue(registries.isEmpty());
        registries.addListener(new ListenerRegistry((Class)TestEvent.class, new JunitTestListener()));
        Assertions.assertFalse(registries.isEmpty());
    }

    @Test
    void removeListener() {
        ListenerRegistries registries = new ListenerRegistries();
        JunitTestListener listener = new JunitTestListener();
        registries.addListener(new ListenerRegistry((Class)TestEvent.class, listener));
        registries.removeListener(listener);
        Assertions.assertTrue(registries.isEmpty());
    }

    @Test
    void getListenersWithPriority() {
        ListenerRegistries registries = new ListenerRegistries();
        JunitTestListener listener = new JunitTestListener();
        registries.addListener(new ListenerRegistry((Class)TestEvent.class, listener));
        Assertions.assertEquals(listener, registries.getListenersWithPriority(Listener.Priority.HIGH).getFirst().listener());
    }

    @Test
    void getListeners() {
        ListenerRegistries registries = new ListenerRegistries();
        JunitTestListener listener = new JunitTestListener();
        registries.addListener(new ListenerRegistry((Class)TestEvent.class, listener));
        Assertions.assertFalse(registries.getListeners().isEmpty());
    }

    @Test
    void callListeners() {
        ListenerRegistries registries = new ListenerRegistries();
        Assertions.assertTrue(registries.isEmpty());
        JunitTestListener listener = new JunitTestListener();
        registries.addListener(new ListenerRegistry((Class)TestEvent.class, listener));
        registries.callListeners(new TestEvent(this), (listenerRegistry, event) -> {
            Assertions.assertEquals(TestEvent.class, event.getClass());
            Assertions.assertEquals(listener, registries.getListeners().getFirst().listener());
        });
    }

    @Test
    void isEmpty() {
        ListenerRegistries registries = new ListenerRegistries();
        Assertions.assertTrue(registries.isEmpty());
        JunitTestListener listener = new JunitTestListener();
        registries.addListener(new ListenerRegistry((Class)TestEvent.class, listener));
        Assertions.assertFalse(registries.isEmpty());
    }

    @Test
    void size() {
        ListenerRegistries registries = new ListenerRegistries();
        Assertions.assertEquals(0, registries.size());
        JunitTestListener listener = new JunitTestListener();
        registries.addListener(new ListenerRegistry((Class)TestEvent.class, listener));
        Assertions.assertEquals(1, registries.size());
    }
}
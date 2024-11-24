package com.erzbir.albaz.dispatch.api;

import com.erzbir.albaz.dispatch.*;
import com.erzbir.albaz.dispatch.internal.PollingEventDispatcher;
import com.erzbir.albaz.logging.Log;
import com.erzbir.albaz.logging.LogFactory;
import org.junit.jupiter.api.Test;

class DefaultDispatcherTest {
    private final Log log = LogFactory.getLog(DefaultDispatcherTest.class);

    @Test
    void dispatch() throws InterruptedException {
        EventDispatcher eventDispatcher = new PollingEventDispatcher();
        EventChannel<NamedEvent> eventEventChannel = GlobalEventChannel.INSTANCE.filterInstance(NamedEvent.class);
        eventDispatcher.start();
        eventEventChannel.subscribe(NamedEvent.class, event -> {
            log.info("Name: " + event.getName());
            return ListenerStatus.CONTINUE;
        });
        EventChannel<Event> filter = GlobalEventChannel.INSTANCE.filter(event -> event instanceof TestEvent);
        filter.subscribe(Event.class, event -> {
            if (event instanceof TestEvent) {
                log.info(((TestEvent) event).getName());
            }
            return ListenerStatus.CONTINUE;
        });
        Listener<TestNamedEvent> listener = new Listener<>() {
            @Override
            public ListenerStatus onEvent(TestNamedEvent event) {
                log.info("this is a TestNamedEvent 2222");
                return ListenerStatus.STOP;
            }

            @Override
            public ConcurrencyKind concurrencyKind() {
                return ConcurrencyKind.LOCKED;
            }
        };
        GlobalEventChannel.INSTANCE.subscribe(TestNamedEvent.class, event -> {
            log.info("this is a TestNamedEvent");
            return ListenerStatus.STOP;
        });
        GlobalEventChannel.INSTANCE.registerListener(TestNamedEvent.class, listener);
        GlobalEventChannel.INSTANCE.subscribe(Event.class, event -> {
            log.info("this is an Event");
            return ListenerStatus.CONTINUE;
        });
        eventDispatcher.dispatch(new TestNamedEvent("this", "test3"), eventEventChannel);
        for (int i = 0; i < 4; i++) {
            eventDispatcher.dispatch(new TestNamedEvent("this", "test2"));

        }
        eventDispatcher.dispatch(new TestNamedEvent("this", "test5"), eventEventChannel);
        eventDispatcher.join();
    }
}
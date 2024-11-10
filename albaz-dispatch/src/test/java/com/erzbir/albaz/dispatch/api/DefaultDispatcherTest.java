package com.erzbir.albaz.dispatch.api;

import com.erzbir.albaz.dispatch.*;
import com.erzbir.albaz.dispatch.internal.PollingEventDispatcher;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class DefaultDispatcherTest {
    @Test
    void dispatch() throws InterruptedException {
        EventDispatcher eventDispatcher = new PollingEventDispatcher();
        EventChannel<NamedEvent> eventEventChannel = GlobalEventChannel.INSTANCE.filterInstance(NamedEvent.class);
        eventDispatcher.start();
        eventEventChannel.subscribe(NamedEvent.class, event -> {
            log.info("Name: {}", event.getName());
            System.out.println("Name: " + event.getName());
            return StandardListenerResult.CONTINUE;
        });
        EventChannel<Event> filter = GlobalEventChannel.INSTANCE.filter(event -> event instanceof TestEvent);
        filter.subscribe(Event.class, event -> {
            if (event instanceof TestEvent) {
                log.info(((TestEvent) event).getName());
            }
            return StandardListenerResult.CONTINUE;
        });
        GlobalEventChannel.INSTANCE.subscribe(TestNamedEvent.class, event -> {
            log.info("this is a TestNamedEvent");
            return StandardListenerResult.CONTINUE;
        });
        GlobalEventChannel.INSTANCE.subscribe(Event.class, event -> {
            log.info("this is an Event");
            return StandardListenerResult.CONTINUE;
        });
        eventDispatcher.dispatch(new TestNamedEvent("this", "test1"));
        eventDispatcher.dispatch(new TestNamedEvent("this", "test2"));
        eventDispatcher.dispatch(new TestNamedEvent("this", "test3"), eventEventChannel);
        eventDispatcher.dispatch(new TestNamedEvent("this", "test4"));
        eventDispatcher.dispatch(new TestEvent("this"));
        eventDispatcher.dispatch(new TestNamedEvent("this", "test5"), eventEventChannel);
        eventDispatcher.cancel();
        eventDispatcher.join();
    }

}
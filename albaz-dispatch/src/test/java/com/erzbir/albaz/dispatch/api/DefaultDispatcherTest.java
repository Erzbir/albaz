package com.erzbir.albaz.dispatch.api;

import com.erzbir.albaz.dispatch.EventDispatcher;
import com.erzbir.albaz.dispatch.channel.EventChannel;
import com.erzbir.albaz.dispatch.event.Event;
import com.erzbir.albaz.dispatch.impl.GlobalEventChannel;
import com.erzbir.albaz.dispatch.internal.PollingEventDispatcher;
import com.erzbir.albaz.dispatch.listener.Listener;
import com.erzbir.albaz.dispatch.listener.ListenerStatus;
import com.erzbir.albaz.logging.Log;
import com.erzbir.albaz.logging.LogFactory;

class DefaultDispatcherTest {
    private static final Log log = LogFactory.getLog(DefaultDispatcherTest.class);


    public static void main(String[] args) throws InterruptedException {
        DefaultDispatcherTest defaultDispatcherTest = new DefaultDispatcherTest();
        EventDispatcher eventDispatcher = new PollingEventDispatcher();
        defaultDispatcherTest.dispatchJoin(eventDispatcher);
        System.out.println(eventDispatcher);
    }

    void dispatch(EventDispatcher eventDispatcher) throws InterruptedException {
        eventDispatcher.start();
        EventChannel<NamedEvent> eventEventChannel = GlobalEventChannel.INSTANCE.filterInstance(NamedEvent.class);
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
        Listener<TestNamedEvent> listener2 = new Listener<>() {
            @Override
            public ListenerStatus onEvent(TestNamedEvent event) {
                log.info("this is a TestNamedEvent 33333");
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
        GlobalEventChannel.INSTANCE.registerListener(TestNamedEvent.class, listener2);

        GlobalEventChannel.INSTANCE.subscribe(Event.class, event -> {
            log.info("this is an Event");
            return ListenerStatus.CONTINUE;
        });
        eventDispatcher.dispatch(new TestNamedEvent("this", "test3"), eventEventChannel);
        for (int i = 0; i < 4; i++) {
            eventDispatcher.dispatch(new TestNamedEvent("this", "test2"));

        }
        eventDispatcher.dispatch(new TestNamedEvent("this", "test5"), eventEventChannel);
    }

    void dispatchJoin(EventDispatcher eventDispatcher) throws InterruptedException {
        dispatch(eventDispatcher);
        eventDispatcher.join();
    }

    void dispatchAwait(EventDispatcher eventDispatcher) throws InterruptedException {
        dispatch(eventDispatcher);
        eventDispatcher.await();
    }
}
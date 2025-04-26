package com.erzbir.albaz.dispatch.internal;

import com.erzbir.albaz.common.Interceptor;
import com.erzbir.albaz.dispatch.channel.EventChannel;
import com.erzbir.albaz.dispatch.event.Event;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Erzbir
 * @since 1.0.0
 */
class NotificationEventDispatcherTest {

    @Test
    void dispatch() throws InterruptedException {
        NotificationEventDispatcher dispatcher = new NotificationEventDispatcher();
        dispatcher.start();
        AtomicBoolean flag = new AtomicBoolean(false);
        EventChannel<Event> eventChannel = dispatcher.getEventChannel();
        eventChannel.subscribeOnce(TestEvent.class, event -> flag.set(true));
        dispatcher.dispatch(new TestEvent(this));
        Thread.sleep(100);
        Assertions.assertTrue(flag.get());
    }

    @Test
    void cancel() throws InterruptedException {
        NotificationEventDispatcher dispatcher = new NotificationEventDispatcher();
        dispatcher.start();
        AtomicBoolean flag = new AtomicBoolean(false);
        EventChannel<Event> eventChannel = dispatcher.getEventChannel();
        eventChannel.subscribeAlways(TestEvent.class, event -> flag.set(true));
        eventChannel.subscribeAlways(NamedEvent.class, event -> flag.set(false));
        Thread thread = new Thread(() -> {
            int i = 10;
            while (i-- > 0) {
                dispatcher.dispatch(new TestEvent(this));
            }
        });
        thread.start();
        Thread.sleep(100);
        dispatcher.close();
        int i = 10;
        while (i-- > 0) {
            dispatcher.dispatch(new TestNamedEvent(this, "as"));
        }
        Assertions.assertTrue(flag.get());
    }

    @Test
    void start() throws InterruptedException {
        NotificationEventDispatcher dispatcher = new NotificationEventDispatcher();
        dispatcher.start();
        AtomicBoolean flag = new AtomicBoolean(false);
        EventChannel<Event> eventChannel = dispatcher.getEventChannel();
        eventChannel.subscribeOnce(TestEvent.class, event -> flag.set(true));
        eventChannel.subscribeOnce(TestNamedEvent.class, event -> flag.set(false));
        dispatcher.dispatch(new TestEvent(this));
        dispatcher.close();
        dispatcher.dispatch(new TestNamedEvent(this, "aop"));
        Thread.sleep(100);
        Assertions.assertTrue(flag.get());

    }

    @Test
    void addInterceptor() {
        NotificationEventDispatcher dispatcher = new NotificationEventDispatcher();
        AtomicBoolean flag = new AtomicBoolean(false);
        dispatcher.addInterceptor(new Interceptor<Event>() {
            @Override
            public boolean intercept(Event target) {
                flag.set(true);
                return true;
            }

            @Override
            public Class<Event> getTargetClass() {
                return null;
            }
        });
    }

    @Test
    void isActive() {
    }

    @Test
    void getEventChannel() {
    }

    @Test
    void join() {
    }

    @Test
    void await() {
    }

    @Test
    void async() {
    }

    @Test
    void of() {
    }

    @Test
    void dispatchTo() {
    }

    @Test
    void dispatchAsync() {
    }


    @Test
    void close() {
    }
}
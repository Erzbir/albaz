package com.erzbir.albaz.dispatch.internal;

import com.erzbir.albaz.common.Interceptor;
import com.erzbir.albaz.dispatch.EventDispatcher;
import com.erzbir.albaz.dispatch.channel.EventChannel;
import com.erzbir.albaz.dispatch.event.Event;
import com.erzbir.albaz.logging.Log;
import com.erzbir.albaz.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <p>
 * 一个抽象的 {@link EventDispatcher}, 实现了开关({@link #start()}, {@link #close()}) 和拦截功能, 子类通过重写 {@link #dispatchTo} 方法实现分发
 * </p>
 *
 * <p>
 * 如果一个 {@link Interceptor} 已经拦截了 {@link Event}, 就不会调用后续的 {@link Interceptor}
 * </p>
 *
 * @author Erzbir
 * @see EventDispatcher
 * @see Interceptor
 * @since 1.0.0
 */
public abstract class AbstractEventDispatcher implements EventDispatcher {
    protected final List<Interceptor<Event>> interceptors = new ArrayList<>();
    protected final AtomicBoolean activated = new AtomicBoolean(false);
    protected final EventChannel<Event> globalEventChannel = GlobalEventChannel.INSTANCE;
    private final Log log = LogFactory.getLog(getClass());

    @Override
    public void dispatch(Event event) {
        dispatch(event, globalEventChannel);
    }

    @Override
    public <E extends Event> void dispatch(E event, EventChannel<E> channel) {
        if (!isActive()) {
            log.warn("EventDispatcher: " + getClass().getSimpleName() + " is already shutdown, dispatching canceled");
            return;
        }
        log.debug(String.format("Dispatching event: %s to channel: %s", event, channel.getClass().getSimpleName()));
        if (!intercept(event)) {
            event.intercepted();
            log.debug("Intercept event: " + event);
        }
        if (event.isIntercepted()) {
            return;
        }
        dispatchTo(event, channel);
    }

    protected abstract <E extends Event> void dispatchTo(E event, EventChannel<E> channel);

    private boolean intercept(Event event) {
        for (Interceptor<Event> interceptor : interceptors) {
            if (interceptor.getTargetClass().isInstance(event)) {
                if (!interceptor.intercept(event)) {
                    return false;
                }
            }
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E extends Event> void addInterceptor(Interceptor<E> dispatchInterceptor) {
        interceptors.add((Interceptor<Event>) dispatchInterceptor);
    }

    @Override
    public boolean isActive() {
        return activated.get();
    }

    @Override
    public EventChannel<Event> getEventChannel() {
        return globalEventChannel;
    }
}


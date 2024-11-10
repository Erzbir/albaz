package com.erzbir.albaz.dispatch.internal;

import com.erzbir.albaz.dispatch.*;
import com.erzbir.albaz.interceptor.DispatchInterceptor;
import com.erzbir.albaz.interceptor.Interceptor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Erzbir
 * @since 1.0.0
 */
@Slf4j
public abstract class AbstractEventDispatcher implements EventDispatcher {
    protected final List<Interceptor<EventContext>> eventDispatchInterceptors = new ArrayList<>();
    protected final AtomicBoolean activated = new AtomicBoolean(false);
    protected InterceptProcessor interceptProcessor = new DefaultInterceptProcessor();

    @Override
    public <E extends Event> void dispatch(E event, EventChannel<E> channel) {
        if (!isActive()) {
            return;
        }
        log.debug("Received event: {}", event);
        DefaultEventContext eventContext = new DefaultEventContext(event);
        if (!intercept(eventContext)) {
            return;
        }
        if (channel.isCanceled()) {
            log.debug("EventChannel: {} is already shutdown, dispatching canceled", channel.getClass().getSimpleName());
            return;
        }
        if (!channel.getListeners().iterator().hasNext()) {
            log.debug("EventChannel: {} has no listener, dispatching canceled", channel.getClass().getSimpleName());
        }
        dispatchTo(event, channel);
    }

    protected abstract <E extends Event> void dispatchTo(E event, EventChannel<E> channel);

    private boolean intercept(EventContext eventContext) {
        return interceptProcessor.intercept(eventContext, eventDispatchInterceptors);
    }

    @Override
    public void addInterceptor(DispatchInterceptor dispatchInterceptor) {
        eventDispatchInterceptors.add(dispatchInterceptor);
    }

    @Override
    public void start() {
        activated.set(true);
    }

    @Override
    public boolean isActive() {
        return activated.get();
    }

    @Override
    public boolean isCanceled() {
        return !activated.get();
    }

    @Override
    public void cancel() {
        activated.set(false);
    }
}

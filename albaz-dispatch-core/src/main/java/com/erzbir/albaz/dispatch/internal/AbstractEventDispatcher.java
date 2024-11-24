package com.erzbir.albaz.dispatch.internal;

import com.erzbir.albaz.common.Interceptor;
import com.erzbir.albaz.dispatch.Event;
import com.erzbir.albaz.dispatch.EventChannel;
import com.erzbir.albaz.dispatch.EventDispatcher;
import com.erzbir.albaz.dispatch.InterceptProcessor;
import com.erzbir.albaz.logging.Log;
import com.erzbir.albaz.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <p>
 * 一个抽象的事件调度器, 实现了开关和拦截功能, 子类通过重写 {@link  #dispatchTo} 方法实现分发
 * </p>
 *
 * @author Erzbir
 * @see EventDispatcher
 * @see Interceptor
 * @see InterceptProcessor
 * @since 1.0.0
 */
public abstract class AbstractEventDispatcher implements EventDispatcher {
    protected final List<Interceptor<Event>> eventDispatchInterceptors = new ArrayList<>();
    protected final AtomicBoolean activated = new AtomicBoolean(false);
    private final Log log = LogFactory.getLog(getClass());
    protected InterceptProcessor interceptProcessor = new InternalInterceptProcessor();

    @Override
    public <E extends Event> void dispatch(E event, EventChannel<E> channel) {
        if (!isActive()) {
            log.warn("EventDispatcher: " + getClass().getSimpleName() + " is already shutdown, dispatching canceled");
            return;
        }
        log.debug("Received event: " + event);
        if (!intercept(event)) {
            return;
        }
        if (channel.isCanceled()) {
            log.warn("EventChannel: " + channel.getClass().getSimpleName() + " is already shutdown, dispatching canceled");
            return;
        }
        dispatchTo(event, channel);
    }

    protected abstract <E extends Event> void dispatchTo(E event, EventChannel<E> channel);

    private boolean intercept(Event event) {
        return interceptProcessor.intercept(event, eventDispatchInterceptors);
    }

    @Override
    public void addInterceptor(Interceptor<Event> dispatchInterceptor) {
        eventDispatchInterceptors.add(dispatchInterceptor);
    }

    @Override
    public void start() {
        activated.compareAndSet(false, true);
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
        activated.compareAndSet(true, false);
    }
}

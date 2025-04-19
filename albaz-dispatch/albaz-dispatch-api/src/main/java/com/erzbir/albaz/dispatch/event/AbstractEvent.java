package com.erzbir.albaz.dispatch.event;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public abstract class AbstractEvent implements Event {
    protected final AtomicBoolean intercepted;
    protected final Object source;
    protected final long timestamp;
    private final AtomicBoolean canceled;
    protected EventContext eventContext;

    public AbstractEvent(Object source) {
        this.source = source;
        intercepted = new AtomicBoolean(false);
        timestamp = System.currentTimeMillis();
        canceled = new AtomicBoolean(false);
    }

    @Override
    public long timestamp() {
        return timestamp;
    }

    @Override
    public boolean isIntercepted() {
        return intercepted.get();
    }

    @Override
    public void intercepted() {
        intercepted.set(true);
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    public void cancel() {
        if (!(this instanceof CancelableEvent)) return;
        canceled.set(true);
    }

    public boolean isCanceled() {
        if (!(this instanceof CancelableEvent)) return false;
        return canceled.get();
    }

    @Override
    public EventContext getContext() {
        if (eventContext == null) {
            eventContext = new EventContext();
            eventContext.setEvent(this);
        }
        return eventContext;
    }

    @Override
    public void setContext(EventContext context) {
        context.setEvent(this);
        this.eventContext = context;
    }

    @Override
    public Object getSource() {
        return source;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "timestamp=" + timestamp +
                ", intercepted=" + intercepted +
                ", canceled=" + canceled +
                '}';
    }
}

package com.erzbir.albaz.dispatch;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public abstract class AbstractEvent implements Event {
    protected final AtomicBoolean intercepted;
    private final AtomicBoolean canceled;
    protected Object source;
    protected long timestamp;

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
        if (!(this instanceof CancelableEvent)) throw new UnsupportedOperationException();
        return canceled.get();
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

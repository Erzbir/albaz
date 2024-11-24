package com.erzbir.albaz.dispatch.internal;


import com.erzbir.albaz.common.Interceptor;
import com.erzbir.albaz.dispatch.channel.EventChannel;
import com.erzbir.albaz.dispatch.channel.ListenerInvoker;
import com.erzbir.albaz.dispatch.event.AbstractEvent;
import com.erzbir.albaz.dispatch.event.Event;
import com.erzbir.albaz.dispatch.listener.Listener;
import com.erzbir.albaz.dispatch.listener.ListenerHandle;
import com.erzbir.albaz.dispatch.listener.ListenerStatus;
import com.erzbir.albaz.logging.Log;
import com.erzbir.albaz.logging.LogFactory;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * <p>
 * 事件通道的内部实现
 * </p>
 *
 * @author Erzbir
 * @see EventChannel
 * @see Event
 * @see Listener
 * @see ListenerStatus
 * @see ListenerRegistry
 * @since 1.0.0
 */
class EventChannelImpl<E extends Event> extends EventChannel<E> {
    protected final Map<Listener<?>, ListenerRegistry> listeners = new ConcurrentHashMap<>();
    private final Log log = LogFactory.getLog(getClass());
    private final Map<Listener<?>, Thread> taskMap = new WeakHashMap<>();

    public EventChannelImpl(Class<E> baseEventClass) {
        super(baseEventClass);
    }

    @Override
    public void broadcast(Event event) {
        if (!(event instanceof AbstractEvent)) throw new IllegalArgumentException("Event must extend AbstractEvent");
        if (event.isIntercepted()) {
            log.debug("Event: {" + event + "} was truncated, cancel broadcast");
            return;
        }
        if (listeners.isEmpty()) {
            log.debug("EventChannel: " + getClass().getSimpleName() + " has no listeners, broadcasting canceled");
        }
        callListeners((AbstractEvent) event);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public <T extends E> ListenerHandle registerListener(Class<T> eventType, Listener<T> listener) {
        SafeListener safeListener = createSafeListener((Listener<E>) listener);
        listeners.put(safeListener, new ListenerRegistry((Class) eventType, safeListener));
        return new WeakListenerHandle(safeListener, listeners.values(), createHandleHook(safeListener));
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public <T extends E> ListenerHandle subscribe(Class<T> eventType, Function<T, ListenerStatus> handle) {
        Listener<E> listener = createListener((Function<E, ListenerStatus>) handle);
        return registerListener((Class<E>) eventType, listener);
    }

    @Override
    public <T extends E> ListenerHandle subscribeOnce(Class<T> eventType, Consumer<T> handle) {
        return subscribe(eventType, event -> {
            handle.accept(event);
            return ListenerStatus.STOP;
        });
    }

    @Override
    public <T extends E> ListenerHandle subscribeAlways(Class<T> eventType, Consumer<T> handle) {
        return subscribe(eventType, event -> {
            handle.accept(event);
            return ListenerStatus.CONTINUE;
        });
    }

    @Override
    public Listener<E> createListener(Function<E, ListenerStatus> handle) {
        return createSafeListener(handle::apply);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public EventChannel<E> filter(Predicate<? extends E> predicate) {
        return new FilterEventChannel(this, predicate);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public <T extends E> EventChannel<T> filterInstance(Class<T> eventType) {
        return new FilterEventChannel(this, eventType);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public Iterable<Listener<E>> getListeners() {
        return (Iterable) listeners.values().stream().map(ListenerRegistry::listener).toList();
    }

    private void callListeners(AbstractEvent event) {
        for (ListenerRegistry listenerRegistry : listeners.values()) {
            if (!listenerRegistry.eventType().isInstance(event)) {
                continue;
            }
            process(listenerRegistry, event);
        }
    }

    private SafeListener createSafeListener(Listener<E> listener) {
        if (listener instanceof SafeListener) {
            return (SafeListener) listener;
        }
        return new SafeListener(listener);
    }

    private boolean intercept(Listener<E> listener) {
        boolean flag = true;
        for (Interceptor<Listener<E>> interceptor : interceptors) {
            flag &= interceptor.intercept(listener);
        }
        return flag;
    }

    /**
     * <p>
     * 在 {@link Listener.ConcurrencyKind#CONCURRENT} 模式下会并发执行监听执行器. {@link Listener.ConcurrencyKind#LOCKED} 模式下则是加锁同步, 会阻塞当前的分发线程
     * </p>
     *
     * @param listenerRegistry 监听器注册表
     * @param event            触发监听的事件
     * @see Listener.ConcurrencyKind
     */
    @SuppressWarnings({"unchecked"})
    private void process(ListenerRegistry listenerRegistry, AbstractEvent event) {
        SafeListener listener = (SafeListener) listenerRegistry.listener();
        String name = listener.delegate.getClass().getSimpleName();
        log.debug("Broadcasting event: " + event + " to listener: " + (name.isEmpty() ? listener.delegate.getClass().getName() : name));
        if (!intercept(listener)) {
            log.info("Listener: " + (name.isEmpty() ? listener.delegate.getClass().getName() : name) + " was intercepted");
            return;
        }
        Runnable invokeRunnable = createInvokeRunnable(event, listener);
        Thread invokeThread = Thread.ofVirtual()
                .name("Listener-Invoke-Thread-" + Thread.currentThread().threadId())
                .start(invokeRunnable);
        taskMap.put(listener, invokeThread);
    }

    @SuppressWarnings({"unchecked"})
    private Runnable createInvokeRunnable(AbstractEvent event, Listener<?> listener) {
        return () -> {
            try {
                SafeListener safeListener = (SafeListener) listener;
                ReentrantLock lock = safeListener.lock;
                if (lock != null) {
                    lock.lock();
                }
                ListenerStatus listenerStatus = listenerInvoker.invoke(new ListenerInvoker.InvokerContext(event, listener));
                if (lock != null) {
                    lock.unlock();
                }
                if (listenerStatus == ListenerStatus.STOP) {
                    safeListener.inactive();
                    listeners.remove(listener);
                }

            } catch (Throwable e) {
                log.error("Calling listener error: " + e.getMessage());
            }
        };
    }

    private Runnable createHandleHook(Listener<E> listener) {
        return () -> {
            Thread thread = taskMap.get(listener);
            if (thread == null) {
                return;
            }
            thread.interrupt();
        };
    }

    abstract static class HookableHandle implements ListenerHandle {
        protected Runnable hook;

        public HookableHandle(Runnable hook) {
            this.hook = hook;
            if (this.hook == null) {
                this.hook = () -> {
                };
            }
        }
    }

    class IndexListenerHandle extends HookableHandle implements ListenerHandle {
        private final AtomicBoolean disposed = new AtomicBoolean(false);

        private final int index;
        private final List<SafeListener> listeners;

        public IndexListenerHandle(int index, List<SafeListener> listeners) {
            this(index, listeners, null);
        }

        public IndexListenerHandle(int index, List<SafeListener> listeners, Runnable hook) {
            super(hook);
            this.index = index;
            this.listeners = listeners;
        }

        @Override
        public void dispose() {
            if (!disposed.compareAndSet(false, true)) {
                return;
            }
            SafeListener remove = listeners.remove(index);
            remove.inactive();
            hook.run();
        }

        @Override
        public boolean isDisposed() {
            return disposed.get();
        }
    }

    class WeakListenerHandle extends HookableHandle implements ListenerHandle {
        private final AtomicBoolean disposed = new AtomicBoolean(false);
        private WeakReference<SafeListener> listenerRef;
        private WeakReference<Collection<ListenerRegistry>> collectionRef;

        public WeakListenerHandle(SafeListener listener, Collection<ListenerRegistry> collection) {
            this(listener, collection, null);
        }

        public WeakListenerHandle(SafeListener listener, Collection<ListenerRegistry> collection, Runnable hook) {
            super(hook);
            this.listenerRef = new WeakReference<>(listener);
            this.collectionRef = new WeakReference<>(collection);
        }


        @Override
        public void dispose() {
            if (!disposed.compareAndSet(false, true)) {
                return;
            }
            Collection<ListenerRegistry> collection = collectionRef.get();
            if (collection != null) {
                SafeListener listener = listenerRef.get();
                if (listener != null) {
                    listener.inactive();
                    collection.removeIf(listenerRegistry -> listenerRegistry.listener().equals(listener));
                }
            }
            listenerRef = null;
            collectionRef = null;
            hook.run();
        }

        @Override
        public boolean isDisposed() {
            return disposed.get();
        }
    }

    /**
     * <p>
     * 一个安全监听器, 通过 {@link EventChannel} 注册的所有监听, 最终都会被包装成此类对象. 捕获所有异常.
     * </p>
     *
     * <p>
     * 这个类实现了 {@link  ConcurrencyKind}, 在执行真正的监听回调之前会检查标志位 {@link  #active}, 判断是否要执行.
     * 这个标志位 {@link #active} 会在监听器返回 {@link  ListenerStatus#STOP} 时被置为 {@code false}.
     * </p>
     *
     * @see Listener
     * @see ConcurrencyKind
     * @see ListenerStatus
     */
    class SafeListener implements Listener<E> {
        private final Log log;
        private final Listener<E> delegate;
        private final AtomicBoolean active = new AtomicBoolean(true);
        private final AtomicBoolean truncated = new AtomicBoolean(false);
        /**
         * 在 {@link  ConcurrencyKind#LOCKED} 模式下, 这个锁会初始化赋值
         */
        private ReentrantLock lock;


        public SafeListener(Listener<E> delegate) {
            this.delegate = delegate;
            log = LogFactory.getLog(delegate.getClass());
            switch (concurrencyKind()) {
                case CONCURRENT -> lock = null;
                case LOCKED -> lock = new ReentrantLock();
            }
        }

        /**
         * 仅内部使用, 在需要的时候可以直接通过此方法设置标志位
         */
        void inactive() {
            active.set(false);
        }

        /**
         * 仅内部使用, 在需要的时候可以直接通过此方法设置标志位
         */
        void truncate() {
            truncated.set(true);
        }

        @Override
        public ConcurrencyKind concurrencyKind() {
            return delegate.concurrencyKind();
        }

        @Override
        public ListenerStatus onEvent(E event) {
            if (Thread.currentThread().isInterrupted()) {
                return ListenerStatus.STOP;
            }
            if (!active.get()) {
                return ListenerStatus.STOP;
            }
            try {
                ListenerStatus listenerStatus = delegate.onEvent(event);
                switch (listenerStatus) {
                    case ListenerStatus.Status.STOP -> active.set(false);
                    case ListenerStatus.Status.TRUNCATED -> truncated.set(true);
                    default -> {
                    }
                }
                return listenerStatus;
            } catch (Throwable e) {
                log.error("Calling listener error: " + e.getMessage());
                return ListenerStatus.TRUNCATED;
            }
        }
    }
}



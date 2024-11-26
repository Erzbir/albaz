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
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * <p>
 * 事件通道的内部实现
 * </p>
 * <p>
 * 基于委派链的设计, 所有广播最终都会委托到 {@link EventChannelDispatcher} 中. 这种委托是链式的,
 * 例如一个 {@link FilterEventChannel} 会将广播委托给下一个 {@link FilterEventChannel},
 * 最终再由 {@link GlobalEventChannel} 委托到 {@link EventChannelDispatcher} 中.
 * </p>
 *
 * <p>
 * 用户注册的 {@link Listener} 会被包装成一个 {@link SafeListener}, 最终封装成一个 {@link ListenerRegistry} 注册到容器中,
 * 并返回一个 {@link ListenerHandle}
 * </p>
 *
 * <p>
 *     TODO 重构为用 forward 实现过滤
 * </p>
 *
 * @author Erzbir
 * @see EventChannel
 * @see Event
 * @see Listener
 * @see ListenerStatus
 * @see ListenerRegistry
 * @see SafeListener
 * @since 1.0.0
 */
class EventChannelImpl<E extends Event> extends EventChannel<E> {
    protected final EnumMap<Listener.Priority, List<ListenerRegistry>> listeners = new EnumMap<>(Listener.Priority.class);
    private final Log log = LogFactory.getLog(getClass());

    public EventChannelImpl(Class<E> baseEventClass) {
        super(baseEventClass);
        listeners.put(Listener.Priority.HIGH, Collections.synchronizedList(new ArrayList<>()));
        listeners.put(Listener.Priority.LOW, Collections.synchronizedList(new ArrayList<>()));
        listeners.put(Listener.Priority.NORMAL, Collections.synchronizedList(new ArrayList<>()));
        listeners.put(Listener.Priority.MONITOR, Collections.synchronizedList(new ArrayList<>()));
    }

    /**
     * <p>
     * 此方法会根据 {@link Listener.ConcurrencyKind} 来决定并发时是否加锁
     * </p>
     *
     * @param event 广播的事件
     * @throws IllegalArgumentException 事件不为 {@link AbstractEvent} 则会抛错
     */
    @Override
    public void broadcast(Event event) {
        if (!(event instanceof AbstractEvent)) throw new IllegalArgumentException("Event must extend AbstractEvent");
        if (event.isIntercepted()) {
            log.debug("Event: {" + event + "} was intercepted, cancel broadcast");
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
        List<ListenerRegistry> listenerRegistries = listeners.get(Listener.Priority.HIGH);
        listenerRegistries.add(new ListenerRegistry((Class) eventType, safeListener));
        return new WeakListenerHandle(safeListener, listenerRegistries, createHandleHook(safeListener));
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
    public Iterable<Listener<?>> getListeners() {
        return (Iterable) listeners.values().stream().flatMap(List::stream).map(ListenerRegistry::listener).toList();
    }

    private void callListeners(AbstractEvent event) {
        List<ListenerRegistry> listenersHigh = listeners.get(Listener.Priority.HIGH);
        List<ListenerRegistry> listenersNormal = listeners.get(Listener.Priority.NORMAL);
        List<ListenerRegistry> listenersLow = listeners.get(Listener.Priority.LOW);
        callListeners0(listenersHigh, event);
        callListeners0(listenersNormal, event);
        callListeners0(listenersLow, event);

    }

    private void callListeners0(List<ListenerRegistry> listeners, AbstractEvent event) {
        for (ListenerRegistry listenerRegistry : listeners) {
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
        String rName = name.isEmpty() ? listener.delegate.getClass().getName() : name;
        log.debug("Broadcasting event: " + event + " to listener: " + rName);
        if (!intercept(listener)) {
            log.info("Listener: " + rName + " was intercepted");
            return;
        }
        Runnable invokeRunnable = createInvokeRunnable(event, listener);
        trigger(listener.triggerType, invokeRunnable);
    }

    private void trigger(Listener.TriggerType triggerType, Runnable runnable) {
        switch (triggerType) {
            case INSTANT -> runnable.run();
            case CONCURRENT -> Thread.ofVirtual()
                    .name("Listener-Invoke-Thread-" + Thread.currentThread().threadId())
                    .start(runnable);
        }
    }

    @SuppressWarnings({"unchecked"})
    private Runnable createInvokeRunnable(AbstractEvent event, Listener<?> listener) {
        return () -> {
            SafeListener safeListener = (SafeListener) listener;
            ReentrantLock lock = safeListener.lock;
            try {
                if (lock != null) {
                    lock.lock();
                }
                ListenerStatus listenerStatus = listenerInvoker.invoke(new ListenerInvoker.InvokerContext(event, listener));
                if (listenerStatus == ListenerStatus.STOP) {
                    safeListener.inactive();
                    new ArrayList<>(listeners.values().stream().flatMap(List::stream).toList()).removeIf(listenerRegistry -> listenerRegistry.listener().equals(listener));
                }
            } catch (Throwable e) {
                log.error("Calling listener error: " + e);
            } finally {
                if (lock != null) {
                    lock.unlock();
                }
            }
        };
    }

    private Runnable createHandleHook(Listener<E> listener) {
        return () -> {

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
            if (isDisposed()) {
                return;
            } else {
                disposed.set(true);
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
        private final Listener.TriggerType triggerType;
        private final AtomicBoolean active = new AtomicBoolean(true);
        private final AtomicBoolean truncated = new AtomicBoolean(false);
        /**
         * 在 {@link  ConcurrencyKind#LOCKED} 模式下, 这个锁会初始化赋值
         */
        private ReentrantLock lock;

        public SafeListener(Listener<E> delegate) {
            this(delegate, TriggerType.CONCURRENT);
        }

        public SafeListener(Listener<E> delegate, Listener.TriggerType triggerType) {
            this.delegate = delegate;
            this.triggerType = triggerType;
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



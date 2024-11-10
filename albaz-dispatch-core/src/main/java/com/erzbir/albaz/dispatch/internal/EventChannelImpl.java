package com.erzbir.albaz.dispatch.internal;


import com.erzbir.albaz.dispatch.*;
import lombok.extern.slf4j.Slf4j;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Erzbir
 * @since 1.0.0
 */
@Slf4j
class EventChannelImpl<E extends Event> extends EventChannel<E> {
    // 为了避免监听器逻辑中出现阻塞, 从而导致监听无法取消
    private final Map<Listener<?>, Thread> taskMap = new WeakHashMap<>();
    protected List<ListenerDescription> listeners = new ArrayList<>();
    protected ListenerInvoker listenerInvoker = new ListenerInvokers.InterceptorInvoker();

    public EventChannelImpl(Class<E> baseEventClass) {
        super(baseEventClass);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public void broadcast(EventContext eventContext) {
        Event event = eventContext.getEvent();
        if (!(event instanceof AbstractEvent)) throw new IllegalArgumentException("Event must extend AbstractEvent");
        if (event.isIntercepted()) {
            log.debug("Event: {} was truncated, cancel broadcast", event);
            return;
        }
        callListeners((E) event);
    }

    @SuppressWarnings({"unchecked"})
    public ListenerHandle registerListener(Class<E> eventType, Listener<E> listener) {
        Listener<E> safeListener = createSafeListener(listener);
        listeners.add(new ListenerDescription((Class<Event>) eventType, safeListener));
        return new WeakReferenceListenerHandle(listener, listeners, createHandleHook(safeListener));
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public <T extends E> ListenerHandle subscribe(Class<T> eventType, Function<T, ListenerResult> handle) {
        Listener<E> listener = createListener((Function<E, ListenerResult>) handle);
        return registerListener((Class<E>) eventType, listener);
    }

    @Override
    public <T extends E> ListenerHandle subscribeOnce(Class<T> eventType, Consumer<T> handle) {
        return subscribe(eventType, event -> {
            handle.accept(event);
            return StandardListenerResult.STOP;
        });
    }

    @Override
    public <T extends E> ListenerHandle subscribeAlways(Class<T> eventType, Consumer<T> handle) {
        return subscribe(eventType, event -> {
            handle.accept(event);
            return StandardListenerResult.CONTINUE;
        });
    }


    @Override
    public Listener<E> createListener(Function<E, ListenerResult> handle) {
        return createSafeListener(handle::apply);
    }


    @Override
    public EventChannel<E> filter(Predicate<Event> predicate) {
        return new FilterEventChannel<>(this, predicate);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public <T extends E> EventChannel<T> filterInstance(Class<T> eventType) {
        FilterEventChannel filterEventChannel = new FilterEventChannel(this, eventType);
        try {
            Field baseEventClass = filterEventChannel.getClass().getSuperclass().getDeclaredField("baseEventClass");
            baseEventClass.setAccessible(true);
            baseEventClass.set(filterEventChannel, eventType);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.error(e.getMessage(), e);
        }
        return filterEventChannel;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public Iterable<Listener<E>> getListeners() {
        return (Iterable) listeners.stream().map(ListenerDescription::listener).toList();
    }

    private void callListeners(E event) {
        for (ListenerDescription listenerDescription : listeners) {
            if (!listenerDescription.eventType().isInstance(event)) {
                continue;
            }
            process(listenerDescription, event);
        }
    }

    private Listener<E> createSafeListener(Listener<E> listener) {
        if (listener instanceof SafeListener) {
            return listener;
        }
        return new SafeListener(listener);
    }

    private boolean interceptProcess(ListenerContext listenerContext) {
        InterceptProcessor interceptProcessor = new DefaultInterceptProcessor();
        return interceptProcessor.intercept(listenerContext, interceptors);
    }


    private void process(ListenerDescription listenerDescription, E event) {
        Listener<?> listener = listenerDescription.listener();
        log.debug("Broadcasting event: {} to listener: {}", event, listener.getClass().getSimpleName());
        if (!interceptProcess(new DefaultListenerContext(new DefaultEventContext(event), listener))) {
            return;
        }
        Thread invokeThread = Thread.ofVirtual()
                .name("Listener-Invoke-Thread")
                .unstarted(createInvokeRunnable(event, listener));
        invokeThread.start();
        taskMap.put(listener, invokeThread);
    }

    private Runnable createInvokeRunnable(E event, Listener<?> listener) {
        return () -> {
            try {
                if (!activated.get()) {
                    Thread.currentThread().interrupt();
                    return;
                }
                ListenerResult listenerResult = listenerInvoker.invoke(new DefaultListenerContext(new DefaultEventContext(event), listener));
                if (!listenerResult.isContinue()) {
                    Thread.currentThread().interrupt();
                }
            } catch (Throwable e) {
                log.error("Calling listener error: {}", e.getMessage());
                Thread.currentThread().interrupt();
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

//    @SuppressWarnings({"unchecked"})
//    @Override
//    public <T extends Event> ListenerHandle register(Class<T> eventType, Listener<T> listener) {
//        return registerListener((Class<E>) eventType, (Listener<E>) listener);
//    }

    public class SafeListener implements Listener<E> {
        private final Listener<E> delegate;

        public SafeListener(Listener<E> delegate) {
            this.delegate = delegate;
        }

        @Override
        public ListenerResult onEvent(E event) {
            try {
                return delegate.onEvent(event);
            } catch (Exception e) {
                return StandardListenerResult.STOP;
            }
        }
    }

    abstract class HookableHandle implements ListenerHandle {
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
        private final List<Listener<?>> listeners;

        public IndexListenerHandle(int index, List<Listener<?>> listeners) {
            this(index, listeners, null);
        }

        public IndexListenerHandle(int index, List<Listener<?>> listeners, Runnable hook) {
            super(hook);
            this.index = index;
            this.listeners = listeners;
        }

        @Override
        public void dispose() {
            if (!disposed.compareAndSet(false, true)) {
                return;
            }
            listeners.remove(index);
            hook.run();
        }

        @Override
        public boolean isDisposed() {
            return disposed.get();
        }
    }

    class WeakReferenceListenerHandle extends HookableHandle implements ListenerHandle {
        private final AtomicBoolean disposed = new AtomicBoolean(false);
        private WeakReference<Listener<?>> listenerRef;
        private WeakReference<Collection<ListenerDescription>> collectionRef;

        public WeakReferenceListenerHandle(Listener<?> listener, Collection<ListenerDescription> collection) {
            this(listener, collection, null);
        }

        public WeakReferenceListenerHandle(Listener<?> listener, Collection<ListenerDescription> collection, Runnable hook) {
            super(hook);
            this.listenerRef = new WeakReference<>(listener);
            this.collectionRef = new WeakReference<>(collection);
        }


        @Override
        public void dispose() {
            if (!disposed.compareAndSet(false, true)) {
                return;
            }
            Collection<ListenerDescription> collection = collectionRef.get();
            if (collection != null) {
                Listener<?> listener = listenerRef.get();
                if (listener != null) {
                    collection.removeIf(listenerDescription -> listenerDescription.listener().equals(listener));
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


}



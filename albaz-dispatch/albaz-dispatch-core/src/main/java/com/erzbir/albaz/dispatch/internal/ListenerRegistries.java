package com.erzbir.albaz.dispatch.internal;

import com.erzbir.albaz.dispatch.event.Event;
import com.erzbir.albaz.dispatch.listener.Listener;
import com.erzbir.albaz.logging.Log;
import com.erzbir.albaz.logging.LogFactory;

import java.util.EnumMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Erzbir
 * @since 1.0.0
 */
final class ListenerRegistries {
    private final Log log = LogFactory.getLog(ListenerRegistries.class);
    private final EnumMap<Listener.Priority, List<ListenerRegistry>> listeners = new EnumMap<>(Listener.Priority.class);

    public ListenerRegistries() {
        listeners.put(Listener.Priority.HIGH, new CopyOnWriteArrayList<>());
        listeners.put(Listener.Priority.NORMAL, new CopyOnWriteArrayList<>());
        listeners.put(Listener.Priority.LOW, new CopyOnWriteArrayList<>());
        listeners.put(Listener.Priority.MONITOR, new CopyOnWriteArrayList<>());
    }

    public void addListener(ListenerRegistry registry) {
        listeners.get(registry.listener().priority()).add(registry);
    }


    public void removeListener(Listener<?> listener) {
        Function<ListenerRegistry, Boolean> function = registry -> registry.listener().equals(listener);
        listeners.get(listener.priority()).removeIf(function::apply);
    }

    public List<ListenerRegistry> getListenersWithPriority(Listener.Priority priority) {
        return listeners.get(priority);
    }

    public List<ListenerRegistry> getListeners() {
        return listeners.values().stream().flatMap(List::stream).collect(Collectors.toList());
    }

    public <E extends Event> void callListeners(E event, Listener.Priority priority, BiConsumer<ListenerRegistry, E> consumer) {
        List<ListenerRegistry> listeners = getListenersWithPriority(priority);
        listeners.stream()
                .filter(registry ->
                        registry.eventType()
                                .isInstance(event))
                .forEach(registry ->
                        consumer.accept(registry, event));
    }

    public <E extends Event> void callListeners(E event, BiConsumer<ListenerRegistry, E> consumer) {
        callListeners(event, Listener.Priority.HIGH, consumer);
        if (event.isIntercepted()) {
            log.debug("Event: [{}] may be intercepted by [{}] listener", event, Listener.Priority.HIGH);
            return;
        }
        callListeners(event, Listener.Priority.NORMAL, consumer);
        if (event.isIntercepted()) {
            log.debug("Event: [{}] may be intercepted by [{}] listener", event, Listener.Priority.NORMAL);
            return;
        }
        callListeners(event, Listener.Priority.LOW, consumer);
        if (event.isIntercepted()) {
            log.debug("Event: [{}] may be intercepted by [{}] listener", event, Listener.Priority.LOW);
            return;
        }
        callListeners(event, Listener.Priority.MONITOR, consumer);
    }

    public boolean isEmpty() {
        return listeners.values().stream().map(List::isEmpty).reduce(Boolean::logicalAnd).orElse(true);
    }

    public int size() {
        return listeners.values().stream().map(List::size).reduce(Integer::sum).orElse(0);
    }
}

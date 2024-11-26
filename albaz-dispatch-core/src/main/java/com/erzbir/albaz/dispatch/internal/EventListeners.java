package com.erzbir.albaz.dispatch.internal;

import com.erzbir.albaz.dispatch.event.Event;
import com.erzbir.albaz.dispatch.listener.Listener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public final class EventListeners {
    private final EnumMap<Listener.Priority, List<ListenerRegistry>> listeners = new EnumMap<>(Listener.Priority.class);

    public EventListeners() {
        listeners.put(Listener.Priority.HIGH, Collections.synchronizedList(new ArrayList<>()));
        listeners.put(Listener.Priority.LOW, Collections.synchronizedList(new ArrayList<>()));
        listeners.put(Listener.Priority.NORMAL, Collections.synchronizedList(new ArrayList<>()));
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
        callListeners(event, Listener.Priority.NORMAL, consumer);
        callListeners(event, Listener.Priority.LOW, consumer);
    }

    public boolean isEmpty() {
        return listeners.values().stream().map(List::isEmpty).reduce(Boolean::logicalOr).orElse(true);
    }

    public int size() {
        return listeners.values().stream().map(List::size).reduce(Integer::sum).orElse(0);
    }
}

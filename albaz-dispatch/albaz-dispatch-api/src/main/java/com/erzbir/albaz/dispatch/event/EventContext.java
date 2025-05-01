package com.erzbir.albaz.dispatch.event;


import com.erzbir.albaz.common.Attribute;
import com.erzbir.albaz.common.AttributeContainer;
import com.erzbir.albaz.common.AttributeMap;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public final class EventContext implements AttributeContainer<Object, Object> {
    private final AttributeContainer<Object, Object> delegate = new AttributeMap<>();
    public final Event event;

    public EventContext(Event event) {
        this.event = event;
    }

    @Override
    public void put(Attribute<Object, Object> attribute) {
        delegate.put(attribute);
    }

    @Override
    public void putIfAbsent(Attribute<Object, Object> attribute) {
        delegate.putIfAbsent(attribute);
    }

    @Override
    public Attribute<Object, Object> remove(Attribute.Key<Object> key) {
        return delegate.remove(key);
    }

    @Override
    public boolean remove(Attribute.Key<Object> key, Attribute<Object, Object> attribute) {
        return delegate.remove(key, attribute);
    }

    @Override
    public Attribute<Object, Object> get(Attribute.Key<Object> key) {
        return delegate.get(key);
    }

    @Override
    public boolean contains(Attribute<Object, Object> attribute) {
        return delegate.contains(attribute);
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public int size() {
        return delegate.size();
    }
}

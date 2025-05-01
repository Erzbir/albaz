package com.erzbir.albaz.common;

import java.util.LinkedHashMap;

/**
 * 基于 {@link LinkedHashMap}
 *
 * @author Erzbir
 * @since 1.0.0
 */
public final class AttributeMap<K, V> extends LinkedHashMap<Attribute.Key<K>, Attribute<K, V>> implements AttributeContainer<K, V> {
    @Override
    public void put(Attribute<K, V> attribute) {
        put(attribute.getKey(), attribute);
    }

    @Override
    public void putIfAbsent(Attribute<K, V> attribute) {
        putIfAbsent(attribute.getKey(), attribute);
    }

    @Override
    public Attribute<K, V> remove(Attribute.Key<K> key) {
        return super.remove(key);
    }

    @Override
    public boolean remove(Attribute.Key<K> key, Attribute<K, V> attribute) {
        return super.remove(key, attribute);
    }

    @Override
    public Attribute<K, V> get(Attribute.Key<K> key) {
        return super.get(key);
    }

    @Override
    public boolean contains(Attribute<K, V> attribute) {
        return containsKey(attribute.getKey());
    }
}

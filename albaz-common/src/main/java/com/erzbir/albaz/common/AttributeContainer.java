package com.erzbir.albaz.common;

/**
 * 属性容器接口
 *
 * @author Erzbir
 * @since 1.0.0
 */
public interface AttributeContainer<K, V> {
    void put(Attribute<K, V> attribute);

    void putIfAbsent(Attribute<K, V> attribute);

    Attribute<K, V> remove(Attribute.Key<K> key);

    boolean remove(Attribute.Key<K> key, Attribute<K, V> attribute);

    Attribute<K, V> get(Attribute.Key<K> key);

    boolean contains(Attribute<K, V> attribute);

    boolean isEmpty();

    int size();
}

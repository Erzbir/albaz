package com.erzbir.albaz.common;

/**
 * 属性容器接口
 *
 * @author Erzbir
 * @since 1.0.0
 */
public interface AttributeContainer<K, V> {
    void putAttribute(Attribute<K, V> attribute);

    void putIfAbsentAttribute(Attribute<K, V> attribute);

    Attribute<K, V> removeAttribute(Attribute.Key<K> key);

    boolean removeAttribute(Attribute.Key<K> key, Attribute<K, V> attribute);

    Attribute<K, V> getAttribute(Attribute.Key<K> key);

    boolean containsAttribute(Attribute<K, V> attribute);
}

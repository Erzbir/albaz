package com.erzbir.albaz.common;

import java.util.Set;
import java.util.function.Supplier;

/**
 * 基于键值对的 {@link Provider}
 *
 * @author Erzbir
 * @see Provider
 * @since 1.0.0
 */
public interface KVProvider<K, V> extends Provider<V> {
    V getInstance(K key);

    V getOrDefault(K key, Supplier<V> fallback);

    Set<K> available();

}

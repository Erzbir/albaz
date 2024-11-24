package com.erzbir.albaz.common;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

/**
 * <p>
 * 只读视图
 * </p>
 *
 * @author Erzbir
 * @since 1.0.0
 */
public interface View {
    <T> T get(Object key) throws NoSuchElementException;

    default <T> T get(Class<T> key) throws NoSuchElementException {
        T v = get((Object) key);
        if (key.isInstance(v)) {
            return v;
        }
        throw new NoSuchElementException("Context does not contain a value of type " + key.getName());
    }

    default <T> T getOrDefault(Object key, T defaultValue) {
        if (!hasKey(key)) {
            return defaultValue;
        }
        return get(key);
    }

    default <T> Optional<T> getOrEmpty(Object key) {
        if (hasKey(key)) {
            return Optional.of(get(key));
        }
        return Optional.empty();
    }

    boolean hasKey(Object key);

    default boolean isEmpty() {
        return size() == 0;
    }

    int size();

    Stream<Map.Entry<Object, Object>> stream();

    default void forEach(BiConsumer<Object, Object> action) {
        stream().forEach(entry -> action.accept(entry.getKey(), entry.getValue()));
    }
}

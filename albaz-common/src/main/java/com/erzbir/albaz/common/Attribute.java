package com.erzbir.albaz.common;

import java.util.Objects;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public interface Attribute<K, V> {
    static Attribute<Object, Object> empty() {
        return new Builder<>(null, null).build();
    }

    Key<K> getKey();

    Attribute<K, V> setKey(K key);

    Value<V> getValue();

    Attribute<K, V> setValue(V value);

    interface Key<E> {
        E getKey();

        Key<E> setKey(E key);
    }

    interface Value<E> {
        E getValue();

        Value<E> setValue(E value);
    }

    class Builder<K, V> {
        Attribute<K, V> attribute;

        public Builder(K key, V value) {
            attribute = new SimpleAttribute(new SimpleKey(key), new SimpleValue(value));
        }

        public Attribute<K, V> build() {
            return attribute;
        }

        private class SimpleKey implements Key<K> {
            K key;

            public SimpleKey(K key) {
                this.key = key;
            }

            @Override
            public K getKey() {
                return key;
            }

            @Override
            public Key<K> setKey(K key) {
                this.key = key;
                return this;
            }

            @Override
            public boolean equals(Object o) {
                if (o == null) return false;
                if (this == o) return true;
                if (o instanceof Key<?> k) return Objects.equals(key, k);
                return false;
            }

            @Override
            public int hashCode() {
                return Objects.hashCode(key);
            }
        }

        private class SimpleValue implements Value<V> {
            V value;

            public SimpleValue(V value) {
                this.value = value;
            }


            @Override
            public V getValue() {
                return value;
            }

            @Override
            public Value<V> setValue(V value) {
                this.value = value;
                return this;
            }

            @Override
            public boolean equals(Object o) {
                if (o == null) return false;
                if (this == o) return true;
                if (o instanceof Value<?> v) return Objects.equals(value, v);
                return false;
            }

            @Override
            public int hashCode() {
                return Objects.hashCode(value);
            }
        }

        private class SimpleAttribute implements Attribute<K, V> {
            private final Key<K> key;
            private final Value<V> value;

            public SimpleAttribute(Key<K> key, Value<V> value) {
                this.key = key;
                this.value = value;
            }

            @Override
            public Key<K> getKey() {
                return key;
            }

            @Override
            public Value<V> getValue() {
                return value;
            }

            @Override
            public Attribute<K, V> setKey(K key) {
                this.key.setKey(key);
                return this;
            }

            @Override
            public Attribute<K, V> setValue(V value) {
                this.value.setValue(value);
                return this;
            }

            @Override
            public boolean equals(Object o) {
                if (o == null) return false;
                if (this == o) return true;
                if (o instanceof Attribute<?, ?> attr) {
                    return Objects.equals(key, attr.getKey()) && Objects.equals(value, attr.getValue());
                }
                return false;
            }

            @Override
            public int hashCode() {
                return Objects.hash(key, value);
            }
        }
    }
}



package com.erzbir.albaz.util;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * ref : spring
 */
public class TypeSafeEnum {
    private final byte key;
    private final String name;

    public TypeSafeEnum(String name, int key) {
        this.name = name;
        if (key > Byte.MAX_VALUE || key < Byte.MIN_VALUE) {
            throw new IllegalArgumentException("key doesn't fit into a byte: " + key);
        }
        this.key = (byte) key;
    }

    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public byte getKey() {
        return key;
    }

    public void write(DataOutputStream s) throws IOException {
        s.writeByte(key);
    }

    @Override
    public int hashCode() {
        return name.hashCode() * 37 + key;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof TypeSafeEnum) &&
                ((TypeSafeEnum) o).key == key &&
                ((TypeSafeEnum) o).name.equals(name);
    }
}


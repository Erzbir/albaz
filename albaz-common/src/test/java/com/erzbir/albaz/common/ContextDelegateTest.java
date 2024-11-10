package com.erzbir.albaz.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ContextDelegateTest {

    @Test
    void put() {
        ContextDelegate contextDelegate = new ContextDelegate();
        contextDelegate.put("key", "value");
        Assertions.assertEquals(1, contextDelegate.size());
        Assertions.assertEquals("value", contextDelegate.get("key"));
    }

    @Test
    void readOnly() {
        ContextDelegate contextDelegate = new ContextDelegate();
        contextDelegate.put("key", "value");
        contextDelegate.readOnly();
        Assertions.assertInstanceOf(View.class, contextDelegate);
        Assertions.assertEquals("value", contextDelegate.get("key"));
    }

    @Test
    void putNonNull() {
        ContextDelegate contextDelegate = new ContextDelegate();
        contextDelegate.putNonNull("key", null);
        Assertions.assertEquals(0, contextDelegate.size());
        contextDelegate.putNonNull("key", "value");
        Assertions.assertEquals("value", contextDelegate.get("key"));
    }

    @Test
    void delete() {
        ContextDelegate contextDelegate = new ContextDelegate();
        contextDelegate.put("key", "value");
        contextDelegate.delete("key");
        Assertions.assertEquals(0, contextDelegate.size());
    }

    @Test
    void get() {
        ContextDelegate contextDelegate = new ContextDelegate();
        contextDelegate.put("key", "value");
        Assertions.assertEquals("value", contextDelegate.get("key"));
    }

    @Test
    void getOrDefault() {
        ContextDelegate contextDelegate = new ContextDelegate();
        contextDelegate.put("key", "value");
        Assertions.assertEquals("value", contextDelegate.getOrDefault("key", "default"));
        Assertions.assertEquals("default", contextDelegate.getOrDefault("non", "default"));
    }

    @Test
    void getOrEmpty() {
        ContextDelegate contextDelegate = new ContextDelegate();
        contextDelegate.put("key", "value");
        Assertions.assertFalse(contextDelegate.getOrEmpty("key").isEmpty());
        Assertions.assertTrue(contextDelegate.getOrEmpty("non").isEmpty());
    }

    @Test
    void hasKey() {
        ContextDelegate contextDelegate = new ContextDelegate();
        contextDelegate.put("key", "value");
        Assertions.assertTrue(contextDelegate.hasKey("key"));
    }

    @Test
    void isEmpty() {
        ContextDelegate contextDelegate = new ContextDelegate();
        contextDelegate.put("key", "value");
        Assertions.assertFalse(contextDelegate.isEmpty());
    }

    @Test
    void size() {
        ContextDelegate contextDelegate = new ContextDelegate();
        contextDelegate.put("key", "value");
        Assertions.assertEquals(1, contextDelegate.size());
    }

    @Test
    void stream() {
        ContextDelegate contextDelegate = new ContextDelegate();
        contextDelegate.put("key", "value");
        Assertions.assertDoesNotThrow(() -> contextDelegate.stream().findFirst());
        Assertions.assertEquals("value", contextDelegate.stream().findFirst().get().getValue());
    }

    @Test
    void forEach() {
        ContextDelegate contextDelegate = new ContextDelegate();
        contextDelegate.put("key", "value");
        contextDelegate.forEach((k, v) -> {
            Assertions.assertEquals("key", k);
            Assertions.assertEquals("value", v);
        });

    }
}
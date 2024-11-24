package com.erzbir.albaz.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ContextNTest {

    @Test
    void put() {
        Context contextN = Context.contextN();
        contextN.put("key", "value");
        Assertions.assertEquals(1, contextN.size());
        Assertions.assertEquals("value", contextN.get("key"));
    }

    @Test
    void readOnly() {
        Context contextN = Context.contextN();
        contextN.put("key", "value");
        contextN.readOnly();
        Assertions.assertInstanceOf(View.class, contextN);
        Assertions.assertEquals("value", contextN.get("key"));
    }

    @Test
    void putNonNull() {
        Context contextN = Context.contextN();
        contextN.putNonNull("key", null);
        Assertions.assertEquals(0, contextN.size());
        contextN.putNonNull("key", "value");
        Assertions.assertEquals("value", contextN.get("key"));
    }

    @Test
    void delete() {
        Context contextN = Context.contextN();
        contextN.put("key", "value");
        contextN.delete("key");
        Assertions.assertEquals(0, contextN.size());
    }

    @Test
    void get() {
        Context contextN = Context.contextN();
        contextN.put("key", "value");
        Assertions.assertEquals("value", contextN.get("key"));
    }

    @Test
    void getOrDefault() {
        Context contextN = Context.contextN();
        contextN.put("key", "value");
        Assertions.assertEquals("value", contextN.getOrDefault("key", "default"));
        Assertions.assertEquals("default", contextN.getOrDefault("non", "default"));
    }

    @Test
    void getOrEmpty() {
        Context contextN = Context.contextN();
        contextN.put("key", "value");
        Assertions.assertFalse(contextN.getOrEmpty("key").isEmpty());
        Assertions.assertTrue(contextN.getOrEmpty("non").isEmpty());
    }

    @Test
    void hasKey() {
        Context contextN = Context.contextN();
        contextN.put("key", "value");
        Assertions.assertTrue(contextN.hasKey("key"));
    }

    @Test
    void isEmpty() {
        Context contextN = Context.contextN();
        contextN.put("key", "value");
        Assertions.assertFalse(contextN.isEmpty());
    }

    @Test
    void size() {
        Context contextN = Context.contextN();
        contextN.put("key", "value");
        Assertions.assertEquals(1, contextN.size());
    }

    @Test
    void stream() {
        Context contextN = Context.contextN();
        contextN.put("key", "value");
        Assertions.assertDoesNotThrow(() -> contextN.stream().findFirst());
        Assertions.assertEquals("value", contextN.stream().findFirst().get().getValue());
    }

    @Test
    void forEach() {
        Context contextN = Context.contextN();
        contextN.put("key", "value");
        contextN.forEach((k, v) -> {
            Assertions.assertEquals("key", k);
            Assertions.assertEquals("value", v);
        });

    }
}
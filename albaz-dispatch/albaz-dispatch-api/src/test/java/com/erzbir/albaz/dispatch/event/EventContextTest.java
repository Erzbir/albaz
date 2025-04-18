package com.erzbir.albaz.dispatch.event;

import com.erzbir.albaz.common.Context;
import com.erzbir.albaz.common.View;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EventContextTest {

    @Test
    void empty() {
        Context empty = Context.empty();
        Assertions.assertTrue(empty.isEmpty());
    }

    @Test
    void contextN() {
        Context context = Context.contextN();
        context.put("key", "value");
        assertFalse(context.isEmpty());

    }

    @Test
    void readOnly() {
        EventContext context = new EventContext();
        context.put("key", "value");
        View view = context.readOnly();
        Assertions.assertFalse(view.isEmpty());
    }

    @Test
    void putNonNull() {
        EventContext context = new EventContext();
        context.putNonNull("key", "value");
        context.putNonNull("key2", null);
        Assertions.assertEquals(1, context.size());
    }

    @Test
    void get() {
        EventContext context = new EventContext();
        context.put("key", "value");
        Assertions.assertEquals("value", context.get("key"));
    }

    @Test
    void getOrDefault() {
        EventContext context = new EventContext();
        Assertions.assertEquals("value", context.getOrDefault("key", "value"));
    }

    @Test
    void getOrEmpty() {
        EventContext context = new EventContext();
        Assertions.assertEquals(Optional.empty(), context.getOrEmpty("key"));
    }

    @Test
    void isEmpty() {
        EventContext context = new EventContext();
        assertTrue(context.isEmpty());
        context.put("key", "value");
        assertFalse(context.isEmpty());
    }

    @Test
    void forEach() {
    }

    @Test
    void getEvent() {
        EventContext context = new EventContext();
        Assertions.assertNull(context.getEvent());
        context.setEvent(new AbstractEvent(this) {
            @Override
            public Object getSource() {
                return this;
            }
        });
        Assertions.assertNotNull(context.getEvent());
    }

    @Test
    void setEvent() {
        EventContext context = new EventContext();
        context.setEvent(new AbstractEvent(this) {
            @Override
            public Object getSource() {
                return this;
            }
        });
        Assertions.assertNotNull(context.getEvent());
    }

    @Test
    void put() {
        EventContext context = new EventContext();
        context.put("key", "value");
        Assertions.assertEquals("value", context.get("key"));
    }

    @Test
    void delete() {
        EventContext context = new EventContext();
        context.put("key", "value");
        context.delete("key");
        Assertions.assertThrows(NoSuchElementException.class, () -> context.get("key"));
    }

    @Test
    void hasKey() {
        EventContext context = new EventContext();
        context.put("key", "value");
        Assertions.assertTrue(context.hasKey("key"));
    }

    @Test
    void size() {
        EventContext context = new EventContext();
        context.put("key", "value");
        Assertions.assertEquals(1, context.size());
    }

    @Test
    void stream() {
        EventContext context = new EventContext();
        context.put("key", "value");
        Assertions.assertEquals(1, context.stream().count());
    }
}
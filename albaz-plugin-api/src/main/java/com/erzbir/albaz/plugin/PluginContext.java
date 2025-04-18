package com.erzbir.albaz.plugin;

import com.erzbir.albaz.common.Context;

import java.util.Map;
import java.util.stream.Stream;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public class PluginContext implements Context {
    private final Context contextDelegate;

    public PluginContext() {
        this.contextDelegate = Context.empty();
    }

    public PluginContext(Context context) {
        this.contextDelegate = context;
    }

    @Override
    public Context put(Object key, Object value) {
        return contextDelegate.put(key, value);
    }

    @Override
    public Context delete(Object key) {
        return contextDelegate.delete(key);
    }

    @Override
    public <T> T get(Object key) {
        return contextDelegate.get(key);
    }

    @Override
    public boolean hasKey(Object key) {
        return contextDelegate.hasKey(key);
    }

    @Override
    public int size() {
        return contextDelegate.size();
    }

    @Override
    public Stream<Map.Entry<Object, Object>> stream() {
        return contextDelegate.stream();
    }
}

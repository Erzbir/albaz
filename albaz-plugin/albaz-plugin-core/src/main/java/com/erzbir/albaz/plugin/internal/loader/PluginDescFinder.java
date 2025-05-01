package com.erzbir.albaz.plugin.internal.loader;

import com.erzbir.albaz.plugin.Plugin;
import com.erzbir.albaz.plugin.PluginHandle;
import com.erzbir.albaz.plugin.PluginDescription;
import com.erzbir.albaz.plugin.exception.PluginIllegalException;
import com.erzbir.albaz.plugin.exception.PluginRuntimeException;

import java.lang.reflect.Field;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public class PluginDescFinder {
    private PluginDescFinder() {
    }

    public static PluginDescription find(PluginHandle pluginHandle) {
        Class<? extends Plugin> pluginClass = pluginHandle.plugin().getClass();
        Field field = find(pluginClass);
        try {
            return (PluginDescription) field.get(pluginHandle.plugin());
        } catch (IllegalAccessException e) {
            throw new PluginIllegalException(String.format("Failed to find description in plugin: %s", pluginHandle.path().getFileName()), e);
        }
    }

    private static Field find(Class<?> c) {
        if (c == null) {
            throw new PluginRuntimeException("Cannot find plugin description for " + c);
        }
        try {
            Field description = c.getDeclaredField("description");
            description.setAccessible(true);
            return description;
        } catch (NoSuchFieldException e) {
            return find(c.getSuperclass());
        }
    }
}

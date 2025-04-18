package org.springframework.cglib.core.internal;

import org.springframework.cglib.core.Customizer;
import org.springframework.cglib.core.KeyFactoryCustomizer;

import java.util.*;

@SuppressWarnings({"rawtypes", "unchecked"})
public class CustomizerRegistry {
    private final Class[] customizerTypes;
    private Map<Class, List<KeyFactoryCustomizer>> customizers = new HashMap<>();

    public CustomizerRegistry(Class[] customizerTypes) {
        this.customizerTypes = customizerTypes;
    }

    /**
     * @deprecated Only to keep backward compatibility.
     */
    @Deprecated
    public static CustomizerRegistry singleton(Customizer customizer) {
        CustomizerRegistry registry = new CustomizerRegistry(new Class[]{Customizer.class});
        registry.add(customizer);
        return registry;
    }

    public void add(KeyFactoryCustomizer customizer) {
        Class<? extends KeyFactoryCustomizer> klass = customizer.getClass();
        for (Class type : customizerTypes) {
            if (type.isAssignableFrom(klass)) {
                List<KeyFactoryCustomizer> list = customizers.computeIfAbsent(type, k -> new ArrayList<>());
                list.add(customizer);
            }
        }
    }

    public <T> List<T> get(Class<T> klass) {
        List<KeyFactoryCustomizer> list = customizers.get(klass);
        if (list == null) {
            return Collections.emptyList();
        }
        return (List<T>) list;
    }
}

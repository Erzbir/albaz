package com.erzbir.albaz.common;

import java.util.List;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public interface Provider<E> {
    E getInstance();

    List<E> getInstances();
}

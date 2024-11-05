package com.erzbir.albaz.event;


import com.erzbir.albaz.common.Context;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public interface EventContext extends Context {
    Event getEvent();
}

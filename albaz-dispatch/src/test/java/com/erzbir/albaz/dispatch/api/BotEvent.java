package com.erzbir.albaz.dispatch.api;

import com.erzbir.albaz.dispatch.Event;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public interface BotEvent extends Event {
    String getBot();
}

package com.erzbir.albaz.dispatch.api.benchmark;

import com.erzbir.albaz.dispatch.event.Event;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public interface BotEvent extends Event {
    String getBot();
}

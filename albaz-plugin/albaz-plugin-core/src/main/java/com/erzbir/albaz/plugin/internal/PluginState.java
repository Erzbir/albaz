package com.erzbir.albaz.plugin.internal;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public enum PluginState {

    CREATED("CREATED"),
    DISABLED("DISABLED"),

    RESOLVED("RESOLVED"),

    STARTED("STARTED"),

    STOPPED("STOPPED"),

    FAILED("FAILED"),

    UNLOADED("UNLOADED");

    private final String status;

    PluginState(String status) {
        this.status = status;
    }

    public boolean isCreated() {
        return this == CREATED;
    }

    public boolean isDisabled() {
        return this == DISABLED;
    }

    public boolean isResolved() {
        return this == RESOLVED;
    }

    public boolean isStarted() {
        return this == STARTED;
    }

    public boolean isStopped() {
        return this == STOPPED;
    }

    public boolean isFailed() {
        return this == FAILED;
    }

    public boolean isUnloaded() {
        return this == UNLOADED;
    }

    public boolean equals(String status) {
        return this.status.equalsIgnoreCase(status);
    }

    @Override
    public String toString() {
        return status;
    }

    public static PluginState parse(String string) {
        for (PluginState status : values()) {
            if (status.equals(string)) {
                return status;
            }
        }

        return null;
    }

}

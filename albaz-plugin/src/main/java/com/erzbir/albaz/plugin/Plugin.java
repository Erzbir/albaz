package com.erzbir.albaz.plugin;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public interface Plugin {
    void onEnable();

    void onDisable();

    void onLoad();

    void onUnLoad();

    void enable();

    void disable();

    boolean isEnable();

    PluginDescription getDescription();

    PluginContext getPluginContext();

    void setPluginContext(PluginContext pluginContext);

}

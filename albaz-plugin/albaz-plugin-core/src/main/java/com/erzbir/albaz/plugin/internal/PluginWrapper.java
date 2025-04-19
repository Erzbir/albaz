package com.erzbir.albaz.plugin.internal;

import com.erzbir.albaz.plugin.Plugin;
import com.erzbir.albaz.plugin.PluginContext;
import com.erzbir.albaz.plugin.PluginDescription;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public class PluginWrapper implements Plugin {
    private final Plugin delegate;

    public PluginWrapper(Plugin plugin) {
        this.delegate = plugin;
    }

    @Override
    public void onEnable() {
        delegate.onEnable();
    }

    @Override
    public void onDisable() {
        delegate.onDisable();
    }

    @Override
    public void onLoad() {
        delegate.onLoad();
    }

    @Override
    public void onUnLoad() {
        delegate.onUnLoad();
    }

    @Override
    public void enable() {
        delegate.enable();
    }

    @Override
    public void disable() {
        delegate.disable();
    }

    @Override
    public boolean isEnable() {
        return delegate.isEnable();
    }

    @Override
    public PluginDescription getDescription() {
        return delegate.getDescription();
    }

    @Override
    public PluginContext getPluginContext() {
        return delegate.getPluginContext();
    }

    @Override
    public void setPluginContext(PluginContext pluginContext) {
        delegate.setPluginContext(pluginContext);
    }
}

package com.erzbir.albaz.plugin;

/**
 * <p>
 * Java 插件继承需这个抽象类
 * </p>
 *
 * @author Erzbir
 * @since 1.0.0
 */
public abstract class JavaPlugin implements Plugin {
    protected final PluginDescription description;
    protected boolean enable = false;
    protected PluginContext pluginContext;

    public JavaPlugin(PluginDescription description) {
        this.description = description;
        this.pluginContext = new PluginContext();
    }

    public JavaPlugin(PluginDescription description, PluginContext pluginContext) {
        this.description = description;
        this.pluginContext = pluginContext;
    }

    @Override
    public PluginDescription getDescription() {
        return description;
    }

    @Override
    public PluginContext getPluginContext() {
        return pluginContext;
    }

    @Override
    public void setPluginContext(PluginContext pluginContext) {
        this.pluginContext = pluginContext;
    }

    @Override
    public void enable() {
        if (enable) {
            return;
        }
        enable = true;
        onEnable();
    }

    @Override
    public void disable() {
        if (!enable) {
            return;
        }
        enable = false;
        onDisable();
    }

    @Override
    public boolean isEnable() {
        return enable;
    }
}

package com.erzbir.albaz.plugin;

/**
 * <p>
 * Java 插件需继承这个抽象类
 * </p>
 *
 * @author Erzbir
 * @since 1.0.0
 */
public abstract class JavaPlugin implements Plugin {
    protected final PluginDescription description;

    public JavaPlugin(PluginDescription description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "JavaPlugin{" +
                "description=" + description +
                '}';
    }
}

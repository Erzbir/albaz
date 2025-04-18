import com.erzbir.albaz.plugin.Plugin;
import com.erzbir.albaz.plugin.PluginManager;

/**
 * <p>
 * 插件模块, 提供插件功能
 * </p>
 *
 * @author Erzbir
 * @since 1.0.0
 */
module albaz.plugin.api {
    uses Plugin;
    uses PluginManager;
    requires transitive albaz.common;
    requires albaz.jcl;
    exports com.erzbir.albaz.plugin;
    exports com.erzbir.albaz.plugin.exception;
    exports com.erzbir.albaz.plugin.spi;
}
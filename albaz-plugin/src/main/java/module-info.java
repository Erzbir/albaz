/**
 * <p>
 * 插件模块, 提供插件功能
 * </p>
 *
 * @author Erzbir
 * @since 1.0.0
 */
module albaz.plugin {
    uses com.erzbir.albaz.plugin.Plugin;
    requires albaz.common;
    requires albaz.jcl;
    exports com.erzbir.albaz.plugin;
    exports com.erzbir.albaz.plugin.exception;
    exports com.erzbir.albaz.plugin.loader;
}
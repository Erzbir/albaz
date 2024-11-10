/**
 * @author Erzbir
 * @since 1.0.0
 */
module com.erzbir.albaz.plugin {
    uses com.erzbir.albaz.plugin.Plugin;
    requires static lombok;
    requires static org.slf4j;
    requires com.erzbir.albaz.common;
    exports com.erzbir.albaz.plugin;
    exports com.erzbir.albaz.plugin.exception;
}
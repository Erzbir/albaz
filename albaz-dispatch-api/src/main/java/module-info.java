/**
 * @author Erzbir
 * @since 1.0.0
 */
module com.erzbir.albaz.dispatch.api {
    requires static lombok;
    requires static org.slf4j;
    requires com.erzbir.albaz.common;
    exports com.erzbir.albaz.dispatch;
    exports com.erzbir.albaz.interceptor;
}
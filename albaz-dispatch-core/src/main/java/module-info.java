/**
 * <p>
 * 此模块为 dispatch-api 的默认实现
 * </p>
 *
 * @author Erzbir
 * @since 1.0.0
 */
module albaz.dispatch.core {
    requires transitive albaz.dispatch.api;
    requires transitive albaz.common;
    requires albaz.jcl;
    exports com.erzbir.albaz.dispatch.internal to albaz.dispatch;
}
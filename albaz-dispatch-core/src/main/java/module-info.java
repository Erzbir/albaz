/**
 * <p>
 * 此模块为 dispatch-api 的默认实现
 * </p>
 *
 * @author Erzbir
 * @since 1.0.0
 */
module com.erzbir.albaz.dispatch.core {
    requires static lombok;
    requires static org.slf4j;
    requires transitive com.erzbir.albaz.dispatch.api;
    requires transitive com.erzbir.albaz.common;
}
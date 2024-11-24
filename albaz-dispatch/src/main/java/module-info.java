/**
 * <p>
 * 连接 api 和 core 的桥, 提供入口
 * </p>
 *
 * @author Erzbir
 * @since 1.0.0
 */
module albaz.dispatch {
    requires albaz.dispatch.core;
    requires albaz.common;
    requires albaz.dispatch.api;
    requires albaz.jcl;
}
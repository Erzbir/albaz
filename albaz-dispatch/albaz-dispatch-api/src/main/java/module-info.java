import com.erzbir.albaz.dispatch.EventDispatcher;

/**
 * <p>
 * 这个模块是调度层的接口模块, 只规定了必须实现的 API,
 * {@link com.erzbir.albaz.dispatch.listener.Listener.ConcurrencyKind} 类似功能交给实现层选择性实现
 * </p>
 *
 * @author Erzbir
 * @since 1.0.0
 */
module com.erzbir.albaz.dispatch.api {
    uses EventDispatcher;
    requires transitive albaz.common;
    exports com.erzbir.albaz.dispatch;
    exports com.erzbir.albaz.dispatch.event;
    exports com.erzbir.albaz.dispatch.listener;
    exports com.erzbir.albaz.dispatch.channel;
    exports com.erzbir.albaz.dispatch.spi;
}
package com.erzbir.albaz.dispatch.listener;

/**
 * <p>
 * 一次监听完成返回的结果, {@link #CONTINUE} 表示继续监听, {@link #STOP} 表示停止监听, {@link #TRUNCATED} 表示截断
 * </p>
 *
 * <p>
 * 通常只需要用到 {@link #CONTINUE} 和 {@link #STOP} 两种, {@link #TRUNCATED} 只发生在某些特殊情况 (例如被拦截或者抛错). 一个 {@link Listener} 进入 {@link #TRUNCATED} 状态后不会被移除, 也不会触发监听 (除非手动移除, 否则不会移除此状态的监听器)
 * </p>
 *
 * @author Erzbir
 * @since 1.0.0
 */
public enum ListenerStatus {
    CONTINUE,
    TRUNCATED,
    STOP
}

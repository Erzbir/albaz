package com.erzbir.albaz.dispatch.event;

import java.util.concurrent.locks.Lock;

/**
 * <p>表示一个事件</p>
 * <p>要广播一个事件需要通过</p>
 *
 * @author Erzbir
 * @since 1.0.0
 */
public interface Event {
    /**
     * @return 事件产生的时间
     */
    long timestamp();

    /**
     * @return 发布事件的主体
     */
    Object getSource();

    /**
     * @return 是否被拦截
     */
    boolean isIntercepted();

    /**
     * <p>
     * 拦截这个事件
     * </p>
     * <p>
     * 如果被拦截器拦截, 调度器不再将事件分发到事件频道, 也不再被广播; 如果是在监听器中拦截, 那么不会再触发更低优先级的监听
     * </p>
     */
    void intercepted();

    Lock getBroadcastLock();

    /**
     * @return 优先级, 值越小优先级越大
     */
    int getPriority();

    EventContext getContext();

    void setContext(EventContext context);

}

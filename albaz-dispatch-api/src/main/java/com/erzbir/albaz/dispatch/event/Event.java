package com.erzbir.albaz.dispatch.event;

import java.util.concurrent.locks.Lock;

/**
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
     * 拦截这个事件. 当事件被拦截之后, 调度器不再将事件分发到事件频道, 也不再被广播
     */
    void intercepted();

    /**
     * @return 优先级
     */
    int getPriority();

    Lock getBroadCastLock();

    EventContext getContext();

    void setContext(EventContext context);

}

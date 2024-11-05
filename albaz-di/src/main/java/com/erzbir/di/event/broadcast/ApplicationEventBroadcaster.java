package com.erzbir.di.event.broadcast;


import com.erzbir.di.event.ApplicationEvent;
import com.erzbir.di.event.listener.ApplicationListener;

/**
 * @author erzbir
 * @since 1.0.0
 */
public interface ApplicationEventBroadcaster {

    /**
     * 添加一个监听器 用于监听所有事件
     *
     * @param listener
     */
    void addApplicationListener(ApplicationListener<?> listener);


    /**
     * 移除一个监听器
     *
     * @param listener
     */
    void removeApplicationListener(ApplicationListener<?> listener);


    /**
     * 将事件通知给对应的监听器
     *
     * @param event
     */
    void broadcastEvent(ApplicationEvent event);
}

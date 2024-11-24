package com.erzbir.albaz.ioc.context;

import com.erzbir.albaz.ioc.beans.factory.ConfigurableListableBeanFactory;

/**
 * @author erzbir
 * @since 1.0.0
 */
public interface ConfigurableApplicationContext extends ApplicationContext {

    /**
     * 刷新容器
     */
    void refresh();

    /**
     * 添加自动销毁钩子
     */
    void registerShutdownHook();

    /**
     * 关闭容器
     */
    void close();

    boolean isActive();

    ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException;
}

package com.erzbir.albaz.di.context.suppport;


import com.erzbir.albaz.di.beans.factory.ConfigurableListableBeanFactory;
import com.erzbir.albaz.di.beans.factory.support.DefaultListableBeanFactory;

/**
 * @author erzbir
 * @since 1.0.0
 */
public abstract class AbstractRefreshableApplicationContext extends AbstractApplicationContext {

    private DefaultListableBeanFactory beanFactory;

    @Override
    protected void refreshBeanFactory() {
        DefaultListableBeanFactory beanFactory = createBeanFactory();
        loadBeanDefinition(beanFactory);
        this.beanFactory = beanFactory;
    }

    protected DefaultListableBeanFactory createBeanFactory() {
        return new DefaultListableBeanFactory();
    }

    /**
     * 加载所有配置的BeanDefinition
     *
     * @param beanFactory
     */
    protected abstract void loadBeanDefinition(DefaultListableBeanFactory beanFactory);


    @Override
    public ConfigurableListableBeanFactory getBeanFactory() {
        return beanFactory;
    }
}

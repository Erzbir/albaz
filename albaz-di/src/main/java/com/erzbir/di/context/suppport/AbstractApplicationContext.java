package com.erzbir.di.context.suppport;


import com.erzbir.di.aop.AnnotationAwareAspectJAutoProxyCreator;
import com.erzbir.di.beans.factory.ConfigurableListableBeanFactory;
import com.erzbir.di.beans.processor.BeanDefinitionPostProcessor;
import com.erzbir.di.beans.processor.BeanPostProcessor;
import com.erzbir.di.beans.processor.support.ApplicationContextAwareProcessor;
import com.erzbir.di.context.ConfigurableApplicationContext;
import com.erzbir.di.event.ApplicationEvent;
import com.erzbir.di.event.ContextClosedEvent;
import com.erzbir.di.event.ContextRefreshedEvent;
import com.erzbir.di.event.broadcast.ApplicationEventBroadcaster;
import com.erzbir.di.event.broadcast.SimpleApplicationEventBroadcaster;
import com.erzbir.di.event.listener.ApplicationListener;
import com.erzbir.di.io.loader.support.DefaultResourceLoader;
import com.erzbir.util.ObjectUtils;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author erzbir
 * @since 1.0.0
 */
public abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigurableApplicationContext {

    public static final String APPLICATION_EVENT_BROADCASTER_BEAN_NAME = "applicationEventBroadcaster";

    private ApplicationEventBroadcaster applicationEventBroadcaster;

    private String displayName = ObjectUtils.identityToString(this);

    private final AtomicBoolean active = new AtomicBoolean();

    private final AtomicBoolean closed = new AtomicBoolean();

    @Override
    public void refresh() {

        // 创建BeanFactory，并加载BeanDefinition
        //扫描xml/注解 把配置的bean封装为BeanDefinition
        //其中会把配置的beanDefinition处理器和Bean处理器添加到BeanDefinitionMap容器中
        closed.set(false);
        active.set(true);
        refreshBeanFactory();

        //获取beanFactory
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();

        // 添加需要用到的Processor
        addPreBeanPostProcessor(beanFactory);

        //注册并执行执行定义的BeanDefinition处理器
        registerBeanDefinitionPostProcessors(beanFactory);

        //BeanPostProcessor 需要提前于其他 Bean 对象实例化之前加到Processors缓存中
        registerBeanPostProcessors(beanFactory);


        //初始化事件通知器
        initApplicationEventBroadcaster();

        // 注册事件监听器
        registerListeners();

        //提前实例化单例Bean对象
        beanFactory.preInstantiateSingletons();

        //发布容器刷新完成事件
        finishRefresh();
    }

    /**
     * 提前添加用到的Processor到IOC
     *
     * @param beanFactory
     */
    private void addPreBeanPostProcessor(ConfigurableListableBeanFactory beanFactory) {
        // 添加ApplicationContextAwareProcessor,让继承自ApplicationContextAware的Bean对象都能感知所属的ApplicationContext
        beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));
        beanFactory.addBeanPostProcessor(new AnnotationAwareAspectJAutoProxyCreator(this));
//		// 添加Aop Processor
//		beanFactory.addBeanPostProcessor (new DefaultAspectJAdvisorFactory());
    }

    /**
     * 初始化事件通知器
     */
    private void initApplicationEventBroadcaster() {
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        applicationEventBroadcaster = new SimpleApplicationEventBroadcaster(beanFactory);
        //注册全局单例通知器
        beanFactory.registerSingleton(APPLICATION_EVENT_BROADCASTER_BEAN_NAME, applicationEventBroadcaster);
    }

    /**
     * 向通知器里添加监听器
     */
    private void registerListeners() {
        //获得所有注册的监听器
        Collection<ApplicationListener> applicationListeners = getBeansOfType(ApplicationListener.class).values();

        //监听器添加到缓存中 用于后续事件发生时通知器通知坚挺者
        for (ApplicationListener listener : applicationListeners) {
            applicationEventBroadcaster.addApplicationListener(listener);
        }

    }

    /**
     * 框架初始化完成后 通知所有监听器
     */
    private void finishRefresh() {
        publishEvent(new ContextRefreshedEvent(this));
    }

    /**
     * 发布事件
     *
     * @param event
     */
    @Override
    public void publishEvent(ApplicationEvent event) {
        applicationEventBroadcaster.broadcastEvent(event);
    }

    /**
     * 更新BeanDefinition容器
     */
    protected abstract void refreshBeanFactory();

    /**
     * 获得BeanFactory
     *
     * @return
     */
    public abstract ConfigurableListableBeanFactory getBeanFactory();

    /**
     * 注册配置的BeanDefinition处理器
     *
     * @param beanFactory
     */
    private void registerBeanDefinitionPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        Map<String, BeanDefinitionPostProcessor> beansOfType = beanFactory.getBeansOfType(BeanDefinitionPostProcessor.class);
        beansOfType.forEach((name, beanDefinitionProcessor) -> {
            beanDefinitionProcessor.postProcessBeanDefinition(beanFactory);
        });
    }

    /**
     * 注册配置的Bean处理器
     *
     * @param beanFactory
     */
    private void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        Map<String, BeanPostProcessor> beansOfType = beanFactory.getBeansOfType(BeanPostProcessor.class);
        beansOfType.forEach((name, processor) -> {
            beanFactory.addBeanPostProcessor(processor);

        });
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) {
        return getBeanFactory().getBeansOfType(type);
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return getBeanFactory().getBeanDefinitionNames();
    }

    @Override
    public void registerShutdownHook() {
        //添加关闭线程
        Runtime.getRuntime().addShutdownHook(new Thread(this::close, "destoryThread"));
    }

    @Override
    public void close() {
        active.set(false);
        // 发布容器关闭事件
        publishEvent(new ContextClosedEvent(this));

        //执行销毁单例bean的销毁方法
        getBeanFactory().destroySingletons();
        closed.set(true);
    }

    @Override
    public Object getBean(String name, Object... args) {
        assertBeanFactoryActive();
        return getBeanFactory().getBean(name, args);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) {
        assertBeanFactoryActive();
        return getBeanFactory().getBean(name, requiredType);
    }

    @Override
    public Object getBean(String name) {
        assertBeanFactoryActive();
        return getBeanFactory().getBean(name);
    }

    @Override
    public <T> T getBean(Class<T> requiredType) {
        assertBeanFactoryActive();
        return getBeanFactory().getBean(requiredType);
    }

    @Override
    public boolean isActive() {
        return active.get();
    }

    protected void assertBeanFactoryActive() {
        if (!this.active.get()) {
            if (this.closed.get()) {
                throw new IllegalStateException(getDisplayName() + " has been closed already");
            } else {
                throw new IllegalStateException(getDisplayName() + " has not been refreshed yet");
            }
        }
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }
}

package com.erzbir.di.aop;


import com.erzbir.di.aop.advisor.Advice;
import com.erzbir.di.aop.advisor.Advisor;
import com.erzbir.di.aop.advisor.MethodMatcher;
import com.erzbir.di.aop.advisor.Pointcut;
import com.erzbir.di.aop.proxy.ProxyFactory;
import com.erzbir.di.aop.proxy.SingletonTargetSource;
import com.erzbir.di.beans.aware.ApplicationContextAware;
import com.erzbir.di.beans.sessions.PropertyValueSession;
import com.erzbir.di.context.ApplicationContext;
import com.erzbir.di.context.SmartInstantiationAwareBeanPostProcessor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author erzbir
 * @since 1.0.0
 */
@Slf4j
public class AnnotationAwareAspectJAutoProxyCreator implements SmartInstantiationAwareBeanPostProcessor, ApplicationContextAware {

    private final AspectJAdvisorFactory advisorFactory = new DefaultAspectJAdvisorFactory();
    /**
     * 记录哪些 bean 尝试过提前创建代理，无论这个 bean 是否创建了代理增强，都记录下来，
     * 等到初始化阶段进行创建代理时，检查缓存，避免重复创建代理。
     * 存储的值就是 beanName
     */
    private final Set<Object> earlyProxyReferences = new HashSet<>();
    private ApplicationContext applicationContext;
    private List<Advisor> cachedAdvisors;

    public AnnotationAwareAspectJAutoProxyCreator(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) {
        return null;
    }

    @Override
    public PropertyValueSession postProcessPropertyValues(PropertyValueSession propertyValueSession, Object bean, String beanName) {
        return null;
    }

    @Override
    public boolean postProcessAfterInstantiation(String beanName, Object bean) {
        return false;
    }

    @Override
    public Object getEarlyBeanReference(Object bean, String beanName) throws RuntimeException {
        this.earlyProxyReferences.add(beanName);
        return wrapIfNecessary(bean, beanName);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (bean != null) {
            // earlyProxyReferences 中不包含当前 beanName，才创建代理
            if (!this.earlyProxyReferences.contains(beanName)) {
                return wrapIfNecessary(bean, beanName);
            } else {
                // earlyProxyReferences 中包含当前 beanName，不再重复进行代理创建，直接返回
                this.earlyProxyReferences.remove(beanName);
            }
        }
        return bean;
    }


    private Object wrapIfNecessary(Object bean, String beanName) {
        if (isInfrastructureClass(bean.getClass())) {
            return bean;
        }
        List<Advisor> advisorList = findEligibleAdvisors(bean.getClass(), beanName);
        if (!advisorList.isEmpty()) {
            return createProxy(bean.getClass(), bean, beanName, advisorList);
        }
        log.trace("Did not attempt to auto-proxy infrastructure class [{}]", beanName);

        return bean;
    }

    protected boolean isInfrastructureClass(Class<?> beanClass) {
        boolean retVal = Advice.class.isAssignableFrom(beanClass) ||
                Pointcut.class.isAssignableFrom(beanClass) ||
                Advisor.class.isAssignableFrom(beanClass) ||
                this.advisorFactory.isAspect(beanClass);
        if (retVal) {
            log.trace("Did not attempt to auto-proxy infrastructure class [{}]", beanClass.getName());
        }
        return retVal;
    }

    private Object createProxy(Class<?> targetClass, Object target, String beanName, List<Advisor> advisorList) {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTargetSource(new SingletonTargetSource(target));
        proxyFactory.addAdvisors(advisorList);
        proxyFactory.setInterfaces(targetClass.getInterfaces());
        log.trace("Create proxy for class [{}], beanName[{}]", targetClass.getName(), beanName);
        return proxyFactory.getProxy();
    }

    private List<Advisor> findEligibleAdvisors(Class<?> beanClass, String beanName) {
        List<Advisor> candidateAdvisors = findCandidateAdvisors();
        List<Advisor> eligibleAdvisors = findAdvisorsThatCanApply(candidateAdvisors, beanClass, beanName);
        // 如果最终的 Advisor 列表不为空，再在开头位置添加一个 ExposeInvocationInterceptor
        // extendAdvisors(eligibleAdvisors);
        if (!eligibleAdvisors.isEmpty()) {
            OrderComparator.sort(eligibleAdvisors);
        }
        return eligibleAdvisors;
    }

    private List<Advisor> findAdvisorsThatCanApply(List<Advisor> candidateAdvisors, Class<?> beanClass, String beanName) {
        if (candidateAdvisors.isEmpty()) {
            return candidateAdvisors;
        }
        List<Advisor> eligibleAdvisors = new ArrayList<>(candidateAdvisors.size());
        Method[] methods = beanClass.getDeclaredMethods();

        // 遍历 bean 目标类型的所有方法，包括继承来的接口方法等
        // 继承的方法没写
        for (Advisor advisor : candidateAdvisors) {
            MethodMatcher methodMatcher = advisor.getPointcut().getMethodMatcher();
            for (Method method : methods) {
                if (methodMatcher.matches(method, beanClass)) {
                    eligibleAdvisors.add(advisor);
                    break;
                }
            }
        }
        return eligibleAdvisors;
    }

    private List<Advisor> findCandidateAdvisors() {
        List<Advisor> advisors = findCandidateAdvisorsInBeanFactory();
        advisors.addAll(findCandidateAdvisorsInAspect());
        return advisors;
    }

    /**
     * 遍历 beanFactory 中所有 bean，找到被 @Aspect 注解标注的 bean，再去 @Aspect 类中封装 Advisor
     *
     * @return
     */
    private List<Advisor> findCandidateAdvisorsInAspect() {
        if (this.cachedAdvisors != null) {
            return this.cachedAdvisors;
        }
        List<Class<?>> allClass = applicationContext.getAllBeanClass();
        List<Advisor> advisors = new ArrayList<>();

        for (Class<?> cls : allClass) {
            if (this.advisorFactory.isAspect(cls)) {
                List<Advisor> classAdvisors = this.advisorFactory.getAdvisors(cls);
                advisors.addAll(classAdvisors);
            }
        }
        this.cachedAdvisors = advisors;
        return this.cachedAdvisors;
    }

    /**
     * 去容器中拿所有低级 Advisor
     *
     * @return
     */
    private List<Advisor> findCandidateAdvisorsInBeanFactory() {
        return new ArrayList<>();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}

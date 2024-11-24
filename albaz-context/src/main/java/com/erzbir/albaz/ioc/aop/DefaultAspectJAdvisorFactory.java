package com.erzbir.albaz.ioc.aop;


import com.erzbir.albaz.ioc.aop.advisor.Advisor;
import com.erzbir.albaz.ioc.aop.advisor.AspectJExpressionPointcut;
import com.erzbir.albaz.ioc.aop.advisor.DefaultPointcutAdvisor;
import com.erzbir.albaz.ioc.aop.advisor.joinpoint.ProceedingJoinPoint;
import com.erzbir.albaz.ioc.aop.anno.*;
import com.erzbir.albaz.ioc.beans.processor.BeanPostProcessor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author erzbir
 * @since 1.0.0
 */
public class DefaultAspectJAdvisorFactory implements AspectJAdvisorFactory, BeanPostProcessor {
    @Override
    public boolean isAspect(Class<?> clazz) {
        return clazz.isAnnotationPresent(Aspect.class);
    }

    @Override
    public List<Advisor> getAdvisors(Class<?> clazz) {
        PrototypeAspectInstanceFactory aspectInstanceFactory = new PrototypeAspectInstanceFactory(clazz);
        List<Advisor> list = new ArrayList<>();

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Before.class)) {
                String expression = method.getAnnotation(Before.class).value();
                AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
                pointcut.setExpression(expression);
                BeforeAdvice advice = new BeforeAdvice(method, aspectInstanceFactory);
                Advisor advisor = new DefaultPointcutAdvisor(pointcut, advice);
                list.add(advisor);
            } else if (method.isAnnotationPresent(After.class)) {
                String expression = method.getAnnotation(After.class).value();
                AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
                pointcut.setExpression(expression);
                AfterAdvice advice = new AfterAdvice(method, aspectInstanceFactory);
                Advisor advisor = new DefaultPointcutAdvisor(pointcut, advice);
                list.add(advisor);
            } else if (method.isAnnotationPresent(AfterReturning.class)) {
                String expression = method.getAnnotation(AfterReturning.class).value();
                AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
                pointcut.setExpression(expression);
                AfterReturningAdvice advice = new AfterReturningAdvice(method, aspectInstanceFactory);
                Advisor advisor = new DefaultPointcutAdvisor(pointcut, advice);
                list.add(advisor);
            } else if (method.isAnnotationPresent(AfterThrowing.class)) {
                AfterThrowing afterThrowing = method.getAnnotation(AfterThrowing.class);
                String expression = afterThrowing.value();
                AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
                pointcut.setExpression(expression);
                AfterThrowingAdvice advice = new AfterThrowingAdvice(method, aspectInstanceFactory);
                advice.setThrowingName(afterThrowing.throwing());
                Advisor advisor = new DefaultPointcutAdvisor(pointcut, advice);
                list.add(advisor);
            } else if (method.isAnnotationPresent(Around.class)) {
                if (method.getParameterCount() == 0) {
                    throw new IllegalStateException("环绕通知的参数中缺少 ProceedingJoinPoint");
                }
                if (!method.getParameterTypes()[0].equals(ProceedingJoinPoint.class)) {
                    throw new IllegalStateException("环绕通知的参数中第一个位置必须是 ProceedingJoinPoint");
                }
                Around around = method.getAnnotation(Around.class);
                String expression = around.value();
                AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
                pointcut.setExpression(expression);
                AroundAdvice advice = new AroundAdvice(method, aspectInstanceFactory);
                Advisor advisor = new DefaultPointcutAdvisor(pointcut, advice);
                list.add(advisor);
            }
        }
        return list;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }
}

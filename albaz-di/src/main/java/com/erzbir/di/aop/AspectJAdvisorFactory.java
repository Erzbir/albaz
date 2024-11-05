package com.erzbir.di.aop;


import com.erzbir.di.aop.advisor.Advisor;

import java.util.List;

/**
 * @author erzbir
 * @since 1.0.0
 */
public interface AspectJAdvisorFactory {

    /**
     * 是否是切面类 @Aspect
     *
     * @param clazz
     * @return
     */
    boolean isAspect(Class<?> clazz);

    /**
     * 解析 @Aspect 切面类中的所有切面
     *
     * @param clazz
     * @return
     */
    List<Advisor> getAdvisors(Class<?> clazz);

}

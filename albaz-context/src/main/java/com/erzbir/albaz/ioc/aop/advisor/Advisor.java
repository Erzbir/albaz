package com.erzbir.albaz.ioc.aop.advisor;


import com.erzbir.albaz.ioc.aop.Ordered;

/**
 * @author erzbir
 * @since 1.0.0
 */
public interface Advisor extends Ordered {

    /**
     * 此方法应该再封装一个接口：PointcutAdvisor，放在这个接口里，这里直接放在 Advisor 接口 里了
     *
     * @return
     */
    Pointcut getPointcut();

    Advice getAdvice();
}

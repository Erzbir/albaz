package com.erzbir.di.context.suppport;


import com.erzbir.di.beans.po.BeanDefinition;
import com.erzbir.di.context.DisposableBean;
import com.erzbir.di.exception.BeanException;
import com.erzbir.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author erzbir
 * @since 1.0.0
 */
public class DisposableBeanAdapter implements DisposableBean {

    private final Object bean;
    private final String beanName;
    private String destroyMethodeName;

    public DisposableBeanAdapter(Object bean, String beanName, BeanDefinition beanDefinition) {
        this.bean = bean;
        this.beanName = beanName;
        this.destroyMethodeName = beanDefinition.getDestroyMethodName();
    }


    @Override
    public void destroy() {
        //1.方式1: 继承DisposableBean接口方式
        if (bean instanceof DisposableBean) {
            ((DisposableBean) bean).destroy();
        }
        // 2.方式2: 配置destroy-method 实现销毁  防止二次销毁
        if (StringUtils.hasText(destroyMethodeName) && !(bean instanceof DisposableBean && "destroy".equals(this.destroyMethodeName))) {
            Method destoryMethod;
            try {
                destoryMethod = bean.getClass().getMethod(destroyMethodeName);
                destoryMethod.invoke(bean);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new BeanException(e);
            }


        }
    }
}

package com.erzbir.albaz.ioc.beans.po;

/**
 * @author erzbir
 * @since 1.0.0
 */


public class BeanReference {
    private String BeanName;

    public BeanReference(String beanName) {
        BeanName = beanName;
    }

    public String getBeanName() {
        return BeanName;
    }

    public void setBeanName(String beanName) {
        BeanName = beanName;
    }
}

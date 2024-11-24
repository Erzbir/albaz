package com.erzbir.albaz.ioc.beans.po;


import com.erzbir.albaz.ioc.beans.factory.ConfigurableBeanFactory;
import com.erzbir.albaz.ioc.beans.sessions.PropertyValueSession;

/**
 * @author erzbir
 * @since 1.0.0
 */

public class BeanDefinition {

    String SCOPE_SINGLETON = ConfigurableBeanFactory.SCOPE_SINGLETON;

    String SCOPE_PROTOTYPE = ConfigurableBeanFactory.SCOPE_PROTOTYPE;

    private Class<?> beanClass;

    private PropertyValueSession propertyValueSession;

    private String initMethodName;

    private String destroyMethodName;

    private String scope = SCOPE_SINGLETON;

    private boolean lazyInit = false;

    private boolean singleton = true;

    private boolean prototype = false;

    public BeanDefinition() {

    }

    public BeanDefinition(Class<?> beanClass) {
        this.beanClass = beanClass;
        this.propertyValueSession = new PropertyValueSession();
    }

    public BeanDefinition(Class<?> beanClass, PropertyValueSession propertyValueSession) {
        this.beanClass = beanClass;
        this.propertyValueSession = propertyValueSession != null ? propertyValueSession : new PropertyValueSession();
    }

    public BeanDefinition(Class<?> beanClass, PropertyValueSession propertyValueSession, String initMethodName, String destroyMethodName) {
        this.beanClass = beanClass;
        this.propertyValueSession = propertyValueSession;
        this.initMethodName = initMethodName;
        this.destroyMethodName = destroyMethodName;
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public PropertyValueSession getPropertyValueSession() {
        return propertyValueSession;
    }

    public void setPropertyValueSession(PropertyValueSession propertyValueSession) {
        this.propertyValueSession = propertyValueSession;
    }

    public String getInitMethodName() {
        return initMethodName;
    }

    public void setInitMethodName(String initMethodName) {
        this.initMethodName = initMethodName;
    }

    public String getDestroyMethodName() {
        return destroyMethodName;
    }

    public void setDestroyMethodName(String destroyMethodName) {
        this.destroyMethodName = destroyMethodName;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
        this.singleton = SCOPE_SINGLETON.equals(scope);
        this.prototype = SCOPE_PROTOTYPE.equals(scope);
    }

    public boolean isLazyInit() {
        return lazyInit;
    }

    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    public boolean isSingleton() {
        return singleton;
    }

    public void setSingleton(boolean singleton) {
        this.singleton = singleton;
    }

    public boolean isPrototype() {
        return prototype;
    }

    public void setPrototype(boolean prototype) {
        this.prototype = prototype;
    }
}

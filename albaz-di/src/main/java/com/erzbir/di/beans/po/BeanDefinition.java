package com.erzbir.di.beans.po;


import com.erzbir.di.beans.factory.ConfigurableBeanFactory;
import com.erzbir.di.beans.sessions.PropertyValueSession;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author erzbir
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
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

    public void setScope(String scope) {
        this.scope = scope;
        this.singleton = SCOPE_SINGLETON.equals(scope);
        this.prototype = SCOPE_PROTOTYPE.equals(scope);
    }
}

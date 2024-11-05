package com.erzbir.di.context.suppport;


import java.util.List;

/**
 * @author erzbir
 * @since 1.0.0
 */
public class ClassPathXmlApplicationContext extends AbstractXmlApplicationContext {

    private String[] configLocations;

    public ClassPathXmlApplicationContext() {

    }

    public ClassPathXmlApplicationContext(String configLocations) {
        this(new String[]{configLocations});
    }

    public ClassPathXmlApplicationContext(String[] configLocations) {
        this.configLocations = configLocations;
        refresh();
    }

    @Override
    protected String[] getConfigLocations() {
        return configLocations;
    }

    @Override
    public List<Class<?>> getAllBeanClass() {
        return getBeanFactory().getAllBeanClass();
    }
}

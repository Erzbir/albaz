package com.erzbir.di.context.suppport;


import com.erzbir.di.beans.factory.support.DefaultListableBeanFactory;
import com.erzbir.di.beans.reader.support.XmlBeanDefinitionReader;

/**
 * @author erzbir
 * @since 1.0.0
 */
public abstract class AbstractXmlApplicationContext extends AbstractRefreshableApplicationContext {


    @Override
    protected void loadBeanDefinition(DefaultListableBeanFactory beanFactory) {
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory, this);
        String[] configurations = getConfigLocations();
        if (configurations != null) {
            beanDefinitionReader.loadBeanDefinitions(configurations);
        }
    }

    /**
     * 获得所有需要加载的Xml文件路径
     *
     * @return
     */
    protected abstract String[] getConfigLocations();


}

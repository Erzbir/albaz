package com.erzbir.di.beans.reader.support;


import com.erzbir.di.beans.annotation.scanner.ClassPathBeanDefinitionScanner;
import com.erzbir.di.beans.factory.ConfigurableBeanFactory;
import com.erzbir.di.beans.po.BeanDefinition;
import com.erzbir.di.beans.po.BeanReference;
import com.erzbir.di.beans.po.PropertyValue;
import com.erzbir.di.beans.registry.BeanDefinitionRegistry;
import com.erzbir.di.beans.sessions.PropertyValueSession;
import com.erzbir.di.exception.BeanException;
import com.erzbir.di.io.loader.ResourceLoader;
import com.erzbir.di.io.loader.support.DefaultResourceLoader;
import com.erzbir.di.io.resource.Resource;
import com.erzbir.util.ProxyUtils;
import com.erzbir.util.StringUtils;
import com.erzbir.util.XmlUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;

/**
 * @author erzbir
 * @since 1.0.0
 */
@Slf4j
public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {
    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader resourceLoader) {
        super(registry, resourceLoader);
    }

    @SneakyThrows
    @Override
    public void loadBeanDefinitions(Resource resource) {
        doLoadBeanDefinitions(resource.getInputStream());
    }

    @SneakyThrows
    @Override
    public void loadBeanDefinitions(Resource... resources) {
        for (Resource resource : resources) {
            loadBeanDefinitions(resource);
        }
    }

    @SneakyThrows
    @Override
    public void loadBeanDefinitions(String... locations) {
        for (String location : locations) {
            ResourceLoader resourceLoader = new DefaultResourceLoader();
            Resource resource = resourceLoader.getResource(location);
            loadBeanDefinitions(resource);
        }
    }

    protected void doLoadBeanDefinitions(InputStream inputStream) {
        BeanDefinitionRegistry registry = getRegistry();
        Document doc = XmlUtil.readXML(inputStream);
        Element root = doc.getDocumentElement();

        //加载带有Bean标签的类
        loadBeanElement(root, registry);

        //加载带有BeanScan标签的获得需要扫描的路径 在路径下扫描注解
        loadBeanScan(root, registry);


    }

    /**
     * 解析 context:bean-scan 标签下的内容 用于组装BeanDefinition
     */
    protected void loadBeanScan(Element root, BeanDefinitionRegistry registry) {
        NodeList beanScanNodeList = root.getElementsByTagName("bean-scan");
        String basePackage = null;
        for (int i = 0; i < beanScanNodeList.getLength(); i++) {
            Element element = (Element) beanScanNodeList.item(i);
            basePackage = element.getAttribute("base-package");

        }

        if (!StringUtils.hasText(basePackage)) {
            log.warn("base-package is empty ");
            return;
        }

        scanPackage(basePackage);
    }

    private void scanPackage(String scnPath) {
        String[] basePackages = StringUtils.delimitedListToStringArray(scnPath, ",");
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(getRegistry());
        scanner.scan(basePackages);
    }

    protected void loadBeanElement(Element root, BeanDefinitionRegistry registry) {

        NodeList beanNodeList = root.getElementsByTagName("bean");
        for (int i = 0; i < beanNodeList.getLength(); i++) {
            Element element = (Element) beanNodeList.item(i);
            NodeList childNodes = element.getChildNodes();
            String id = element.getAttribute("id");
            String name = element.getAttribute("name");
            String className = element.getAttribute("class");
            String initMethodName = element.getAttribute("init-method");
            String destroyMethodName = element.getAttribute("destroy-method");
            String beanScope = element.getAttribute("scope");
            try {
                String beanName = id;
                if (beanName.isEmpty()) beanName = name;

                Class<?> clazz;

                clazz = ProxyUtils.getDefaultClassLoader().loadClass(className);

                if (beanName.isEmpty())
                    beanName = StringUtils.lowerFirst(clazz.getSimpleName());
                PropertyValueSession propertyValueSession = doLoadByPropertyValue(element);
                BeanDefinition beanDefinition = new BeanDefinition(clazz, propertyValueSession);

                if (!initMethodName.isEmpty()) {
                    beanDefinition.setInitMethodName(initMethodName);
                }

                if (!destroyMethodName.isEmpty()) {
                    beanDefinition.setDestroyMethodName(destroyMethodName);
                }

                if (StringUtils.hasText(beanScope)) {
                    if (beanScope.equals(ConfigurableBeanFactory.SCOPE_PROTOTYPE) || beanScope.equals(ConfigurableBeanFactory.SCOPE_SINGLETON))
                        beanDefinition.setScope(beanScope);
                    else throw new BeanException("property [scope] exception :" + beanScope);
                }

                if (registry.containsBeanDefinition(beanName)) {
                    throw new BeanException("Duplicate beanName[ " + beanName + "] is not allowed");
                }

                registry.registerBeanDefinition(beanName, beanDefinition);

            } catch (ClassNotFoundException e) {

                throw new BeanException(className + "can not be instantiated", e);
            }
        }
    }

    /**
     * 处理bean的set方法注入方式
     */
    private PropertyValueSession doLoadByPropertyValue(Element element) {
        NodeList propertyList = element.getElementsByTagName("property");
        PropertyValueSession propertyValueSession = new PropertyValueSession();

        for (int i = 0; i < propertyList.getLength(); i++) {
            Element e = (Element) propertyList.item(i);
            String name = e.getAttribute("name");
            String value = e.getAttribute("value");
            String ref = e.getAttribute("ref");
            Object o = null;
            if (StringUtils.hasText(ref)) {
                o = new BeanReference(ref);
            } else {
                o = value;
            }
            propertyValueSession.addPropertyValue(new PropertyValue(name, o));
        }
        return propertyValueSession;
    }

}

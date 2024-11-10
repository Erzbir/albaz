package com.erzbir.albaz.di.beans.sessions;


import com.erzbir.albaz.di.beans.po.PropertyValue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author erzbir
 * @since 1.0.0
 */
public class PropertyValueSession {
    /**
     * 每个类的属性缓存
     */
    private final List<PropertyValue> propertyValueList = new ArrayList<>();

    public void addPropertyValue(PropertyValue propertyValue) {
        this.propertyValueList.add(propertyValue);
    }

    public PropertyValue[] getPropertyValues() {
        return this.propertyValueList.toArray(new PropertyValue[0]);
    }

    /**
     * 根据属性值名字获取属性的PropertyValue
     *
     * @param propertyName
     * @return
     */
    public PropertyValue getPropertyValue(String propertyName) {
        return propertyValueList.stream()
                .filter(propertyValue -> propertyValue.getName().equals(propertyName))
                .collect(Collectors.toList())
                .get(0);
    }

}

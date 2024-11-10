package com.erzbir.albaz.di.beans.annotation.support;


import com.erzbir.albaz.di.beans.processor.support.PropertyPlaceholderProcessor;

import java.util.Properties;

/**
 * @author erzbir
 * @since 1.0.0
 */
public class PlaceholderResolvingStringValueResolver implements StringValueResolver {

    private final Properties properties;

    public PlaceholderResolvingStringValueResolver(Properties properties) {
        this.properties = properties;
    }


    @Override
    public String resolveStringValue(String strVal) {
        return PropertyPlaceholderProcessor.resolvePlaceHolder(strVal, properties);
    }
}

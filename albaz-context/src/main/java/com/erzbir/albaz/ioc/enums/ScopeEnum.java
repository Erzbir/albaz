package com.erzbir.albaz.ioc.enums;

/**
 * @author erzbir
 * @since 1.0.0
 */
public enum ScopeEnum {

    /**
     * 单例
     */
    PROTOTYPE("prototype"),
    /**
     * 原型
     */
    SINGLETON("singleton");

    private final String types;

    ScopeEnum(String types) {
        this.types = types;
    }

    public String getTypes() {
        return types;
    }
}

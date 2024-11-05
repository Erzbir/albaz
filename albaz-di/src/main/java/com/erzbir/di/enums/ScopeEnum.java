package com.erzbir.di.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author erzbir
 * @since 1.0.0
 */
@AllArgsConstructor
@Getter
public enum ScopeEnum {

    /**
     * 单例
     */
    PROTOTYPE("prototype"),
    /**
     * 原型
     */
    SINGLETON("singleton");

    private String types;

}

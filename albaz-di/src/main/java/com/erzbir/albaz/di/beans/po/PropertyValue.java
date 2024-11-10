package com.erzbir.albaz.di.beans.po;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author erzbir
 * @since 1.0.0
 */
@Getter
@Setter
@AllArgsConstructor
public class PropertyValue {
    /**
     * bean的名字 一般为类名首字母小写
     */
    private final String name;
    /**
     * 分两种类型
     * 1>自定义类的引用类型
     * 2>包装类型 Integer Double 等
     */
    private final Object value;


}

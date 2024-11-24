package com.erzbir.albaz.ioc.exception;

import com.erzbir.albaz.util.NestedRuntimeException;

/**
 * @author erzbir
 * @since 1.0.0
 */
public class BeanException extends NestedRuntimeException {


    public BeanException(String message) {
        super(message);
    }

    public BeanException(String message, Throwable cause) {
        super(message, cause);
    }

    public BeanException(Throwable e) {
        super(e.getMessage(), e);
    }
}

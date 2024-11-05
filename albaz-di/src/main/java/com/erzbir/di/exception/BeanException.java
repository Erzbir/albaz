package com.erzbir.di.exception;

import com.erzbir.util.NestedRuntimeException;

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

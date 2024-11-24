package com.erzbir.albaz.dispatch;


import com.erzbir.albaz.common.Interceptor;

/**
 * <p>
 * 监听执行器, 监听器并不直接执行监听回调, 而是通过这个接口来执行
 * </p>
 *
 * <p>
 * 可以添加拦截器, 拦截器通过 {@link  InterceptProcessor} 来执行
 * </p>
 *
 * @author Erzbir
 * @see InterceptProcessor
 * @since 1.0.0
 */
public interface ListenerInvoker {
    ListenerStatus invoke(InvokerContext invokerContext);

    void addInterceptor(Interceptor<InvokerContext> interceptor);
}
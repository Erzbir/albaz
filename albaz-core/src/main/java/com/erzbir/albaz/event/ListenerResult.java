package com.erzbir.albaz.event;

/**
 * 监听完成返回的结果
 *
 * @author Erzbir
 * @since 1.0.0
 */
public interface ListenerResult {
    boolean isTruncated();

    boolean isContinue();
}

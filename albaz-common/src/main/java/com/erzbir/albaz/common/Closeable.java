package com.erzbir.albaz.common;

/**
 * <p>
 * 表示资源生命周期结束, 比如文件或连接等
 * </p>
 *
 * @author Erzbir
 * @since 1.0.0
 */
public interface Closeable {
    void close();
}

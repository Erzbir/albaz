package com.erzbir.di;


import org.springframework.cglib.proxy.Enhancer;

/**
 * @author Erzbir
 * @since 1.0.0
 */
public class ProxyTest {
    public static void main(String[] args) throws Throwable {
        DaoProxy daoProxy = new DaoProxy();

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(Dao.class);
        enhancer.setCallback(daoProxy);

        Dao dao = (Dao) enhancer.create();
        dao.update();
        dao.select();
    }
}
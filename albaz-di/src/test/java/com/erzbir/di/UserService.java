package com.erzbir.di;

import com.erzbir.albaz.di.beans.annotation.Autowired;
import com.erzbir.albaz.di.beans.annotation.Component;
import com.erzbir.albaz.di.beans.annotation.Scope;
import com.erzbir.albaz.di.beans.aware.BeanNameAware;
import com.erzbir.albaz.di.context.InitializingBean;
import com.erzbir.albaz.di.enums.ScopeEnum;

/**
 * @author mafei007
 * @date 2022/6/29 19:31
 */
@Component("userService")
@Scope(ScopeEnum.SINGLETON)
// @Scope("prototype")
public class UserService implements BeanNameAware, InitializingBean, UserInterface {

    @Autowired
    private OrderService orderService;

    @Override
    public void test(int i) {
        int k = 1 / 1;
        // String a = null;
        // System.out.println(a.length());
        System.out.println("userService#test 方法执行。。。。。参数 i 为：" + i + "。 依赖注入的 com.erzbir.di.OrderService：" + orderService);
    }

    @Override
    public void setBeanName(String beanName) {
        System.out.println("我是：" + this.getClass().getName() + "，我的 beanName是：" + beanName);
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("我是：" + this.getClass().getName() + "，开始执行初始化方法：afterPropertiesSet()");
    }
}

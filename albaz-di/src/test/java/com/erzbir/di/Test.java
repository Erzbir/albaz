package com.erzbir.di;

import com.erzbir.albaz.di.beans.annotation.ComponentScan;
import com.erzbir.albaz.di.context.suppport.AnnotationConfigApplicationContext;

/**
 * @author mafei007
 * @date 2022/6/29 19:27
 */
@ComponentScan("com.erzbir.di")
public class Test {
    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext annotationApplicationContext = new AnnotationConfigApplicationContext(ProxyTest.class);
//        annotationApplicationContext.refresh();
        System.out.println("=================beanNames=================");
        for (String beanName : annotationApplicationContext.getBeanDefinitionNames()) {
            System.out.println(beanName);
        }
        System.out.println("===========================================");

        // JDK 代理对象返回的是 com.sun.proxy.$Proxy5， 不能强转为实现，只能强转为接口
        // com.erzbir.di.UserService userService = (com.erzbir.di.UserService) applicationContext.getBean("userService");
        UserInterface userService = (UserInterface) annotationApplicationContext.getBean("userService");
        System.out.println("userService: " + userService.getClass().getName());
        userService.test(100);
        System.out.println("applicationContext.getBean(\"orderService\"):  " + annotationApplicationContext.getBean("orderService"));
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>");
        userService.toString();

        // PostInterface postService = (PostInterface) applicationContext.getBean("postService");
        // postService.post();
        // System.out.println(postService.getClass().getName());
    }
}

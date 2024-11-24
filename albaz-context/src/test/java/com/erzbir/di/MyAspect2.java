package com.erzbir.di;

import com.erzbir.albaz.ioc.aop.anno.After;
import com.erzbir.albaz.ioc.aop.anno.AfterReturning;
import com.erzbir.albaz.ioc.aop.anno.Aspect;
import com.erzbir.albaz.ioc.aop.anno.Before;
import com.erzbir.albaz.ioc.beans.annotation.Component;

@Aspect
@Component
public class MyAspect2 {

    @Before("execution(* *.Main.*())")
    public void f3() {
        System.out.println("before 2 通知....");
    }


    @AfterReturning("execution(* *.PostService.*())")
    public void f4() {
        System.out.println("AfterReturning 1 通知....");
    }


    @AfterReturning("execution(* *.PostService.*())")
    public void f5() {
        System.out.println("AfterReturning 2 通知....");
    }

    @Before("execution(* *.PostService.*())")
    public void f1() {
        System.out.println("before 1 通知....");
    }

    @After("execution(* *.PostService.*())")
    public void f2() {
        System.out.println("after 通知....");
    }


    @After("execution(* *.Main.*())")
    public void f11() {
        System.out.println("after 通知 in Aspect2 ....");
    }


}

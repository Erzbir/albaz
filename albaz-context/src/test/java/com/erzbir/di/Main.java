package com.erzbir.di;


import com.erzbir.albaz.ioc.beans.annotation.Component;
import com.erzbir.albaz.ioc.beans.annotation.ComponentScan;
import com.erzbir.albaz.ioc.context.suppport.AnnotationConfigApplicationContext;

@Component("main")
@ComponentScan("com.erzbir")
public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext("com.erzbir");
        Main main = (Main) applicationContext.getBean("main");
        main.say();
    }


    @Handler
    @Filter
    public void say() {
        System.out.println("Hello world!");
    }
}
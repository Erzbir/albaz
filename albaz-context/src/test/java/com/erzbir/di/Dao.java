package com.erzbir.di;

import com.erzbir.albaz.ioc.beans.annotation.Component;

@Component("dao")
public class Dao {

    public void update() {
        System.out.println("PeopleDao.update()");
    }

    public void select() {
        System.out.println("PeopleDao.select()");
    }
}

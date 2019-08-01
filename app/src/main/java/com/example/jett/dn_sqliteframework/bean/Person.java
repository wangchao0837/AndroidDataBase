package com.example.jett.dn_sqliteframework.bean;

import com.example.jett.dn_sqliteframework.sqlite.DbField;
import com.example.jett.dn_sqliteframework.sqlite.DbTable;

/**
 * Created by Jett on 2017/10/2.
 */
@DbTable("tb_person")
public class Person {
    @DbField("name")
    String name;
    @DbField("age")
    Integer age;

    public Person(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
}

package com.example.jett.dn_sqliteframework.bean;

import com.example.jett.dn_sqliteframework.sqlite.DbField;
import com.example.jett.dn_sqliteframework.sqlite.DbTable;

/**
 * Created by Jett on 2017/10/2.
 */
@DbTable("tb_user")
public class User {
    @DbField("_id")
    private String id;
    @DbField("name")
    private String name;
    @DbField("password")
    private String password;
    public Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public User() {
    }

    public User(String id, String name, String password, Integer status) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.status = status;
    }

    public User(String id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

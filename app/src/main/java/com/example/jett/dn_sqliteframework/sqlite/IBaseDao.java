package com.example.jett.dn_sqliteframework.sqlite;

import java.util.List;

/**
 * Created by Jett on 2017/10/2.
 * insert update  delete select*  batchInsert batchUpdate.......
 */

public interface IBaseDao<T> {
    /**
     * 插入操作
     */
    long insert(T entity);

    int update(T entity,T where);

    int delete(T where);

    List<T> query(T where);

    List<T> query(T where,String orderBy,Integer startIndex,Integer limit);

}













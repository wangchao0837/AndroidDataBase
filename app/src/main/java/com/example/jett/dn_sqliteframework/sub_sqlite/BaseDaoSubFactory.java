package com.example.jett.dn_sqliteframework.sub_sqlite;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.jett.dn_sqliteframework.sqlite.BaseDao;
import com.example.jett.dn_sqliteframework.sqlite.BaseDaoFactory;

/**
 * 通过单例工厂让调用层获取数据库操作 用实例
 */

public class BaseDaoSubFactory extends BaseDaoFactory {
    private  BaseDaoSubFactory() {
    }
    private static final BaseDaoSubFactory ourInstance = new BaseDaoSubFactory();

    public static BaseDaoSubFactory getInstance() {
        return ourInstance;
    }

    //用于实现分库
    private SQLiteDatabase subSqliteDatabase;


    public  synchronized  <T extends BaseDao<M>,M> T
    getSubDao(Class<T> daoClass,Class<M> entityClass)
    {
        BaseDao baseDao=null;
        if(map.get(daoClass.getSimpleName())!=null){
            return (T)map.get(PrivateDataBaseEnums.database.getValue());
        }
        Log.i("jett","生成数据库文件"+ PrivateDataBaseEnums.database.getValue());
        //生成数据库文件/data/data/com.example.jett.dn_sqliteframework/update/N0002/logic.db
        subSqliteDatabase=SQLiteDatabase.openOrCreateDatabase(PrivateDataBaseEnums.database.getValue(),null);

        try {
            baseDao=daoClass.newInstance();
            baseDao.init(subSqliteDatabase,entityClass);
            map.put(PrivateDataBaseEnums.database.getValue(),baseDao);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return (T) baseDao;
    }

}











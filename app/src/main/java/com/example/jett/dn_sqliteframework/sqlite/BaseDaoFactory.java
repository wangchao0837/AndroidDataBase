package com.example.jett.dn_sqliteframework.sqlite;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 通过单例工厂让调用层获取数据库操作 用实例
 */

public class BaseDaoFactory {

    private static final BaseDaoFactory ourInstance = new BaseDaoFactory();

    public static BaseDaoFactory getInstance() {
        return ourInstance;
    }

    private SQLiteDatabase sqLiteDatabase;

    private String sqliteDatabasePath;

    //保存所有的dao层，实现单例
    protected Map<String, BaseDao> map = Collections.synchronizedMap(new HashMap<String, BaseDao>());
    //用于实现分库
    private SQLiteDatabase subSqliteDatabase;

    protected BaseDaoFactory() {
        //建议写入SD卡
        File file = new File(Environment.getExternalStorageDirectory(), "update");
        if (!file.exists()) {
            file.mkdirs();
        }
        sqliteDatabasePath = file.getAbsolutePath() + "/user.db";
//        File file=new File("data/data/com.example.jett.dn_sqliteframework/update");
//        file.mkdir();
//        sqliteDatabasePath="data/data/com.example.jett.dn_sqliteframework/update"+"/user.db";

        sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(sqliteDatabasePath, null);

    }

    public synchronized <T> BaseDao<T> getBaseDao(Class<T> entityClass) {
        BaseDao baseDao = null;
        try {
            baseDao = BaseDao.class.newInstance();
            baseDao.init(sqLiteDatabase, entityClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baseDao;
    }

    public synchronized <T extends BaseDao<M>, M> T getBaseDao(Class<T> daoClass, Class<M> entityClass) {
        BaseDao baseDao = null;
        if (map.get(daoClass.getSimpleName()) != null) {
            return (T) map.get(daoClass.getSimpleName());
        }
        try {
            baseDao = daoClass.newInstance();
            baseDao.init(sqLiteDatabase, entityClass);
            map.put(daoClass.getSimpleName(), baseDao);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return (T) baseDao;
    }


//    public  synchronized  <T extends  BaseDao<M>,M> T
//    getSubDao(Class<T> daoClass,Class<M> entityClass)
//    {
//        BaseDao baseDao=null;
//
//        if(map.get(daoClass.getSimpleName())!=null){
//            return (T)map.get(PrivateDataBaseEnums.database.getValue());
//        }
//
//        Log.i("jett","生成数据库文件"+ PrivateDataBaseEnums.database.getValue());
//        //生成数据库文件/data/data/com.example.jett.dn_sqliteframework/update/N0002/logic.db
//        subSqliteDatabase=SQLiteDatabase.openOrCreateDatabase(PrivateDataBaseEnums.database.getValue(),null);
//
//
//        try {
//            baseDao=daoClass.newInstance();
//            baseDao.init(subSqliteDatabase,entityClass);
//            map.put(PrivateDataBaseEnums.database.getValue(),baseDao);
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//        return (T) baseDao;
//    }

}











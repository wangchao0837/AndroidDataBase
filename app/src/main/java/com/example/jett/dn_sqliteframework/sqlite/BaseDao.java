package com.example.jett.dn_sqliteframework.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Jett on 2017/10/2.
 */

public class BaseDao<T> implements IBaseDao<T> {
    //持有数据库操作的引用
    private SQLiteDatabase sqLiteDatabase;
    //持有操作数据库所对应的java类型
    private Class<T> entityClass;
    //表名
    private String tableName;
    //标记,用来记录是否做过初始化
    private boolean isInit = false;
    //定义一个缓存空间(key-字段名  value-成员变量
    private HashMap<String, Field> cacheMap;


    //自动建数据库和表   初始化
    public  boolean init(SQLiteDatabase sqLiteDatabase, Class<T> entityClass) {
        this.sqLiteDatabase = sqLiteDatabase;
        this.entityClass = entityClass;
        if (!isInit) {
            //自动建表
            //取到表名
            if (entityClass.getAnnotation(DbTable.class) != null) {
                tableName = entityClass.getAnnotation(DbTable.class).value();
            } else {
                tableName = entityClass.getSimpleName();
            }
            //确定表字段名
            if (!sqLiteDatabase.isOpen()) {
                return false;
            }
            //执行建表
            String createTableSql = getCreateTableSql();
            sqLiteDatabase.execSQL(createTableSql);
            //初始化缓存==列名和成员变量
            cacheMap = new HashMap();
            initCacheMap();
            isInit = true;
        }
        return isInit;
    }

    private String getCreateTableSql() {
        //create table if not exists tb_user(_id integer,name varchar(20),password varchar(20))
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("create table if not exists ");
        stringBuffer.append(tableName + "(");
        //反射得到所有的成员变量
        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            Class type = field.getType();
            if (field.getAnnotation(DbField.class) != null) {
                if (type == String.class) {
                    stringBuffer.append(field.getAnnotation(DbField.class).value() + " TEXT,");
                } else if (type == Integer.class) {
                    stringBuffer.append(field.getAnnotation(DbField.class).value() + " INTEGER,");
                } else if (type == Long.class) {
                    stringBuffer.append(field.getAnnotation(DbField.class).value() + " BIGINT,");
                } else if (type == Double.class) {
                    stringBuffer.append(field.getAnnotation(DbField.class).value() + " DOUBLE,");
                } else if (type == byte[].class) {
                    stringBuffer.append(field.getAnnotation(DbField.class).value() + " BLOB,");
                } else {
                    //不支持的类型
                    continue;
                }
            } else {
                if (type == String.class) {
                    stringBuffer.append(field.getName() + " TEXT,");
                } else if (type == Integer.class) {
                    stringBuffer.append(field.getName() + " INTEGER,");
                } else if (type == Long.class) {
                    stringBuffer.append(field.getName() + " BIGINT,");
                } else if (type == Double.class) {
                    stringBuffer.append(field.getName() + " DOUBLE,");
                } else if (type == byte[].class) {
                    stringBuffer.append(field.getName() + " BLOB,");
                } else {
                    //不支持的类型
                    continue;
                }
            }
        }
        if (stringBuffer.charAt(stringBuffer.length() - 1) == ',') {
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        }
        stringBuffer.append(")");
        return stringBuffer.toString();

    }

    private void initCacheMap() {
        //1.取所有的列名====(查空表)
        String sql = "select * from " + tableName + " limit 1,0";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        String[] columnNames = cursor.getColumnNames();
        //2.取所有的成员变量(反射)
        Field[] columnFields = entityClass.getDeclaredFields();
        //3.进行列名和成员变量的映射,存入到缓存中
        for (Field field : columnFields) {
            field.setAccessible(true);
        }
        for (String columnName : columnNames) {
            Field columnFiled = null;
            for (Field field : columnFields) {
                String fieldName = null;
                if (field.getAnnotation(DbField.class) != null) {
                    fieldName = field.getAnnotation(DbField.class).value();
                } else {
                    fieldName = field.getName();
                }
                if (columnName.equals(fieldName)) {
                    columnFiled = field;
                    break;
                }
            }
            if (columnFiled != null) {
                cacheMap.put(columnName, columnFiled);
            }
        }
    }

    @Override
    public long insert(T entity) {
        //准备好ContentValues中需要用的数据
        Map<String, String> map = getValues(entity);
        //设置插入的内容
        ContentValues values = getContentValues(map);
        //执行插入
        long result = sqLiteDatabase.insert(tableName, null, values);
        return result;
    }


    private Map<String, String> getValues(T entity) {
        HashMap<String, String> map = new HashMap<>();
        Iterator<Field> fieldIterator = cacheMap.values().iterator();
        while (fieldIterator.hasNext()) {
            Field field = fieldIterator.next();
            field.setAccessible(true);
            //获取成员变量的值
            try {
                Object object = field.get(entity);
                if (object == null) {
                    continue;
                }
                String value = object.toString();
                //获取列名
                String key = null;
                if (field.getAnnotation(DbField.class) != null) {
                    key = field.getAnnotation(DbField.class).value();
                } else {
                    key = field.getName();
                }
                if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
                    map.put(key, value);
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    private ContentValues getContentValues(Map<String, String> map) {
        ContentValues contentValues = new ContentValues();
        Set keys = map.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = map.get(key);
            if (value != null) {
                contentValues.put(key, value);
            }
        }
        return contentValues;
    }

    @Override
    public int update(T entity, T where) {
//        sqLiteDatabase.update(tableName,contentValues,"name=?",new String[]{"张三"});
        int result = -1;
        Map values = getValues(entity);
        ContentValues contentValues=getContentValues(values);

        Map whereCause=getValues(where);
        Condition condition=new Condition(whereCause);
        Log.i("jett",contentValues.size()+"");
        result=sqLiteDatabase.update(tableName,contentValues,condition.whereCause,condition.whereArgs);
        Log.i("jett","result="+result+" tableName="+tableName);
        return result;
    }

    @Override
    public int delete(T where) {
//        sqLiteDatabase.delete(tableName,"name=?",new String[]{"jett"});
        Map map=getValues(where);
        Condition condition=new Condition(map);
        int result=sqLiteDatabase.delete(tableName,condition.whereCause,condition.whereArgs);
        return result;
    }

    private class Condition {
        private String whereCause;//"name=? && password=?...."
        private String[] whereArgs;//new String[]{"张三"}
        public Condition(Map<String,String> whereCause){
            ArrayList list=new ArrayList();
            StringBuilder stringBuilder=new StringBuilder();
            stringBuilder.append("1=1 ");
            Set keys=whereCause.keySet();
            Iterator iterator=keys.iterator();
            while(iterator.hasNext()){
                String key=(String)iterator.next();
                String value=whereCause.get(key);
                if(value!=null){
                    stringBuilder.append(" and "+key+"=?");
                    list.add(value);
                }
            }
            this.whereCause=stringBuilder.toString();
            this.whereArgs= (String[]) list.toArray(new String[list.size()]);
        }


    }

    @Override
    public List<T> query(T where) {
        return query(where,null,null,null);
    }

    @Override
    public List<T> query(T where, String orderBy, Integer startIndex, Integer limit) {
        Map map=getValues(where);

        String limitString=null;
        if(startIndex!=null&&limit!=null)
        {
            limitString=startIndex+" , "+limit;
        }

        Condition condition=new Condition(map);
        Cursor cursor=sqLiteDatabase.query(tableName,null,condition.whereCause
                ,condition.whereArgs,null,null,orderBy,limitString);
        List<T> result=getResult(cursor,where);
        cursor.close();
        return result;
    }

    private List<T> getResult(Cursor cursor, T where) {
        ArrayList list=new ArrayList();

        Object item;
        while (cursor.moveToNext())
        {
            try {
                item=where.getClass().newInstance();
                /**
                 * 列名  name
                 * 成员变量名  Filed;
                 */
                Iterator iterator=cacheMap.entrySet().iterator();
                while (iterator.hasNext())
                {
                    Map.Entry entry= (Map.Entry) iterator.next();
                    /**
                     * 得到列名
                     */
                    String colomunName= (String) entry.getKey();
                    /**
                     * 然后以列名拿到  列名在游标的位子
                     */
                    Integer colmunIndex=cursor.getColumnIndex(colomunName);

                    Field field= (Field) entry.getValue();

                    Class type=field.getType();
                    if(colmunIndex!=-1)
                    {
                        if(type==String.class)
                        {
                            //反射方式赋值
                            field.set(item,cursor.getString(colmunIndex));
                        }else if(type==Double.class)
                        {
                            field.set(item,cursor.getDouble(colmunIndex));
                        }else  if(type==Integer.class)
                        {
                            field.set(item,cursor.getInt(colmunIndex));
                        }else if(type==Long.class)
                        {
                            field.set(item,cursor.getLong(colmunIndex));
                        }else  if(type==byte[].class)
                        {
                            field.set(item,cursor.getBlob(colmunIndex));
                            /*
                            不支持的类型
                             */
                        }else {
                            continue;
                        }
                    }

                }
                list.add(item);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        return list;
    }



}









package com.example.jett.dn_sqliteframework.sub_sqlite;

import android.os.Environment;

import com.example.jett.dn_sqliteframework.bean.User;
import com.example.jett.dn_sqliteframework.sqlite.BaseDaoFactory;

import java.io.File;

/**
 *
 */

public enum  PrivateDataBaseEnums {
    /**
     * 存放本地数据库的路径
     */
    database("");
    /**
     * 文件存储的文件路径
     */
    private String value;
    PrivateDataBaseEnums(String value )
    {
        this.value = value;
    }

    public String getValue()
    {
        UserDao userDao= BaseDaoFactory.getInstance().getBaseDao(UserDao.class,User.class);
        if(userDao!=null)
        {
            User currentUser=userDao.getCurrentUser();
            if(currentUser!=null)
            {
//                File file=new File(Environment.getExternalStorageDirectory(),"update");
//                File file=new File("/data/data/com.example.jett.dn_sqliteframework");
//                if(!file.exists())
//                {
//                    file.mkdirs();
//                }
                //建议写入SD卡
                File file=new File(Environment.getExternalStorageDirectory(),"update/"+currentUser.getId());
                if(!file.exists())
                {
                    file.mkdirs();
                }

                return file.getAbsolutePath()+"/login.db";
            }

        }
        return value;
    }



}

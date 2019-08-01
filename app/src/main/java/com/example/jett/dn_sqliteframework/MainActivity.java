package com.example.jett.dn_sqliteframework;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.jett.dn_sqliteframework.bean.Photo;
import com.example.jett.dn_sqliteframework.bean.User;
import com.example.jett.dn_sqliteframework.sqlite.BaseDao;
import com.example.jett.dn_sqliteframework.sqlite.BaseDaoFactory;
import com.example.jett.dn_sqliteframework.sqlite.BaseDaoImplA;
import com.example.jett.dn_sqliteframework.sqlite.IBaseDao;
import com.example.jett.dn_sqliteframework.sub_sqlite.BaseDaoSubFactory;
import com.example.jett.dn_sqliteframework.sub_sqlite.PhotoDao;
import com.example.jett.dn_sqliteframework.sub_sqlite.UserDao;
import com.example.jett.dn_sqliteframework.update.UpdateManager;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    UserDao userDao;
    UpdateManager updateManager;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        updateManager = new UpdateManager();
        userDao = BaseDaoFactory.getInstance().getBaseDao(UserDao.class, User.class);
    }

    public void clickLogin(View view) {
        User user = new User();
//        user.setName("V00"+(i++));
        user.setPassword("123456");
        user.setName("张三" + (++i));
        user.setId("n000" + i);
        userDao.insert(user);
        Toast.makeText(this, "执行成功!", Toast.LENGTH_SHORT).show();
    }

    public void clickSubInsert(View view) {
        Photo photo = new Photo();
        photo.setPath("data/data/my.jpg");
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        photo.setTime(dateFormat.format(new Date()));
        PhotoDao photoDao = BaseDaoSubFactory.getInstance().getSubDao(PhotoDao.class, Photo.class);
        photoDao.insert(photo);
        Toast.makeText(this, "执行成功!", Toast.LENGTH_SHORT).show();
    }


    public void clickInsert(View view) {
        IBaseDao<User> userDao = BaseDaoFactory.getInstance().getBaseDao(BaseDaoImplA.class, User.class);
        userDao.insert(new User("1", "a", "aa", 1));
        User where = new User();
        Toast.makeText(this, "执行成功!", Toast.LENGTH_SHORT).show();
    }

    public void clickUpdate(View view) {
        BaseDao<User> userDao = BaseDaoFactory.getInstance().getBaseDao(User.class);
        User where = new User();
        where.setId("1");
        User user = new User();
        user.setName("jett");
        userDao.update(user, where);
    }

    public void clickDelete(View view) {
        BaseDao<User> userDao = BaseDaoFactory.getInstance().getBaseDao(User.class);
        User where = new User();
        where.setId("1");
        userDao.delete(where);
    }

    public void clickSelect(View view) {
        BaseDao<User> userDao = BaseDaoFactory.getInstance().getBaseDao(User.class);
        User where = new User();
        where.setId("1");
        List<User> list = userDao.query(where);
        Log.i("jett", list.size() + "");
    }

    public void write(View view) {
        updateManager.saveVersionInfo(this, "V003");
    }

    public void update(View view) {
        updateManager.checkThisVersionTable(this);
        updateManager.startUpdateDb(this);
    }


}











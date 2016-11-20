package com.example.administrator.mycontacts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.EditText;

/**
 * Created by Administrator on 2016/11/20.
 */
import java.util.Vector; //数据表类
public class ContactsTable {
    //数据名常量
    private final static String TABLENAME = "contactsTable";
    //声明数据库对象
    private MyDB db;
    //构造方法
    public ContactsTable(Context context) {
       //创建MYDB对象 context很重要
        db = new MyDB(context);
        //如果数据表不存在就新建数据表
        if (!db.isTableExits(TABLENAME)) {
            String createTableSql = "CREATE TABLE IF NOT EXISTS" + TABLENAME + "id_DB integer" +
                    "primary key AUTOINCREMENT,"+
                    User.NAME+"VARCHAR,"+
                    User.MOBILE+"VARCHAR,"+
                    User.DANWEI+"VARCHAR,"+
                    User.QQ+"VARCHAR,"+
                    User.ADDRESS+"VARCHAR)";
            db.createTable(createTableSql);
        }
    }
    //增加数据
    public boolean addData(User user){
        //创建ContentValues对象用于保存数据
        ContentValues values  = new ContentValues();
        //contentValues赋值
        values.put(User.NAME,user.getName());
        values.put(User.MOBILE,user.getMobile());
        values.put(User.DANWEI,user.getDanwei());
        values.put(User.QQ,user.getQq());
        values.put(User.ADDRESS,user.getAddress());
        //保存数据
        return db.save(TABLENAME,values);
    }

    //step2
    //获取全部联系人数据
    public User[]  getAllUser(){
        Vector<User> v = new Vector<>();
        Cursor cursor  = null;
        try {
            cursor = db.find("select * from"+TABLENAME,null);
            while (cursor.moveToNext()){
                User temp = new User();
                temp.setId_DB(cursor.getInt(cursor.getColumnIndex("id_DB")));
                temp.setName(cursor.getString(cursor.getColumnIndex(User.NAME)));
                temp.setAddress(cursor.getString(cursor.getColumnIndex(User.ADDRESS)));
                temp.setMobile(cursor.getString(cursor.getColumnIndex(User.MOBILE)));
                temp.setDanwei(cursor.getString(cursor.getColumnIndex(User.DANWEI)));
                temp.setQq(cursor.getString(cursor.getColumnIndex(User.QQ)));
                v.add(temp);
            }
        }catch (Exception  e){
            e.printStackTrace();
        }finally {
            if (cursor!=null){
                cursor.close();
            }
            db.closeConnection();
        }
        if (v.size()>0){
            return v.toArray(new User[]{});
        }else {
            User [] users = new User[1];
            User user = new User();
            user.setName("木有结果");
            users[0]=user;
            return users;
        }
    }
    //用id查找联系人
    public User getUserByID(int id){
        Cursor cursor  = null;
        User  temp = new User();
        try {
            cursor = db.find("select * from"+ TABLENAME+"where"+"id_DB=?",new String[]{id+""});

            cursor.moveToNext();
            temp.setId_DB(cursor.getInt(cursor.getColumnIndex("id_DB")));
            temp.setName(cursor.getString(cursor.getColumnIndex(User.NAME)));
            temp.setAddress(cursor.getString(cursor.getColumnIndex(User.ADDRESS)));
            temp.setMobile(cursor.getString(cursor.getColumnIndex(User.MOBILE)));
            temp.setDanwei(cursor.getString(cursor.getColumnIndex(User.DANWEI)));
            temp.setQq(cursor.getString(cursor.getColumnIndex(User.QQ)));
            Log.d("aa",temp.getName());
            return temp;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor!=null){
                cursor.close();
            }
            db.closeConnection();
        }
        return null;
    }
    public boolean updateUser(User user){
        ContentValues values = new ContentValues();

        values.put(User.NAME,user.getName());
        values.put(User.MOBILE,user.getMobile());
        values.put(User.DANWEI,user.getDanwei());
        values.put(User.QQ,user.getQq());
        values.put(User.ADDRESS,user.getAddress());
        return db.update(TABLENAME,values,"id_DB=?",new String[]{user.getId_DB()+""});
    }
    public User[]  findUserByKey(String key){
        Vector<User> v =  new Vector<>();
        Cursor cursor = null;

        try {
            cursor = db.find("select * from"+TABLENAME+"where"+
                    User.NAME+"like'%"+key+"%'"+
                    "or"+User.MOBILE+"like '%"+key+"%'"+
                    "or"+User.QQ+"like '%"+key+"%'",null);
            while (cursor.moveToNext()){
                User temp = new User();
                temp.setId_DB(cursor.getInt(cursor.getColumnIndex("id_DB")));
                temp.setName(cursor.getString(cursor.getColumnIndex(User.NAME)));
                temp.setMobile(cursor.getString(cursor.getColumnIndex(User.MOBILE)));
                temp.setDanwei(cursor.getString(cursor.getColumnIndex(User.DANWEI)));
                temp.setQq(cursor.getString(cursor.getColumnIndex(User.QQ)));
                temp.setAddress(cursor.getString(cursor.getColumnIndex(User.ADDRESS)));
                v.add(temp);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor!=null){
                cursor.close();
            }
            db.closeConnection();
        }
        if (v.size()>0){
            return v.toArray(new User[]{});
        }else {
            User [] users = new User[1];
            User user = new User();
            user.setName("木有结果");
            users[0]=user;
            return users;
        }
    }
    public boolean deleteByUser(User user){
        return db.delete(TABLENAME,"id_DB=?",new  String[]{user.getId_DB()+""});
    }
}


package com.example.firstdemo.user;

import com.example.firstdemo.mysql.JDBC_Delete;
import com.example.firstdemo.mysql.JDBC_Manager;

import java.sql.SQLException;

class Delete_User {
    /*
    * 增加一些成员变量
    * */
    User user;
    JDBC_Manager jdbc_manager;
    JDBC_Delete jdbc_delete;

    Delete_User(User user)
        throws SQLException,ClassNotFoundException {
        this.user=user;
        jdbc_manager=new JDBC_Manager();
        jdbc_delete=new JDBC_Delete(jdbc_manager);
    }

    //删除某表中的数据,两个判断条件
    public boolean delete_user_info(int type,String table_name,String[] column_name,Object[] objects){
        return jdbc_delete.Delete(type,table_name,column_name,objects);
    }

    //删除某表中的数据，一个判断条件
    public boolean delete_user_info(int type,String table_name,String column_name,Object objects){
        return jdbc_delete.Delete(type,table_name,column_name,objects);
    }

    //删除某表中的所有数据
    public boolean delete_user_info(int type,String table_name){
        return jdbc_delete.Delete(type,table_name);
    }

}

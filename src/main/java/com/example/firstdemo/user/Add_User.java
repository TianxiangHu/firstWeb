package com.example.firstdemo.user;

import com.example.firstdemo.mysql.JDBC_Insert;
import com.example.firstdemo.mysql.JDBC_Manager;

import java.sql.SQLException;

public class Add_User {
    /*
     * 增加一些成员变量
     * */
    User user;
    JDBC_Manager jdbc_manager;
    JDBC_Insert jdbc_insert;

    Add_User(User user)
        throws SQLException,ClassNotFoundException {
        this.user=user;
        jdbc_manager=new JDBC_Manager();
        jdbc_insert=new JDBC_Insert(jdbc_manager);
    }

     //支持一次插入表的一行数据
    public boolean add_User_Info(String table_name,String[] columns,String[] values) {
        /*switch (table_name) {
            case "role":
                return jdbc_insert.Insert(table_name,columns,values);
            case "user_info":
                return jdbc_insert.Insert(table_name,columns,values);
            case "user_role":
                return jdbc_insert.Insert(table_name,columns,values);
        }*/
        return jdbc_insert.Insert(table_name,columns,values);
    }

}

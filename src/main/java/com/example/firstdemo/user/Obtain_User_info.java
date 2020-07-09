package com.example.firstdemo.user;

import com.example.firstdemo.mysql.JDBC_Manager;
import com.example.firstdemo.mysql.JDBC_Select;

import java.sql.SQLException;
import java.util.List;

/*
* 这个类用来获取成员信息，包括"role","user_info","user_role"三个表
* */
public class Obtain_User_info {
    /*
     * 增加一些成员变量
     * */
    User user;
    JDBC_Manager jdbc_manager;
    JDBC_Select jdbc_select;

    Obtain_User_info(User user)
        throws SQLException,ClassNotFoundException{
        this.user=user;
        jdbc_manager=new JDBC_Manager();
        jdbc_select=new JDBC_Select(jdbc_manager);
    }

    //通过某一个参数来查找并初始化某一指定表格内容
    public boolean init_specific_table(int type,String table_name,String column,String value){
        List<String> ls=null;
        ls=jdbc_select.Query(type,table_name,column,value);
        if(ls!=null){
            return fill_table(ls,table_name);
        }
        return false;
    }

    //通过某两个参数来查找并初始化某一指定表格内容
    public boolean init_specific_table(int type,String table_name,String[] column,String[] value){
        List<String> ls=null;
        ls=jdbc_select.Query(type,table_name,column,value);
        if(ls!=null){
            return fill_table(ls,table_name);
        }
        return false;
    }

    public boolean init_adr(String value) {
        List<String> ls = null;
        String sql = "select adr from " + user.getTable_adr() + " where user_id=" + value;
        ls = jdbc_select.Query(sql);
        if (ls != null) {
            user.setAdr(ls);
            return true;
        }
        return false;
    }

    //一个辅助函数
    private boolean fill_table(List<String> ls,String table_name){
        String[] values=new String[ls.size()];
        ls.toArray(values);
        switch (table_name) {
            case "role":
                return user.getRole(values);
            case "user_info":
                return user.getUserInfo(values);
            case "user_role":
                return user.getUser_Role(values);
            case "user_adr":
                user.setAdr(ls);
                return true;
        }
        return false;
    }

}

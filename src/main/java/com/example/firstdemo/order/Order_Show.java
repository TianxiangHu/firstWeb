package com.example.firstdemo.order;

import com.example.firstdemo.mysql.JDBC_Manager;
import com.example.firstdemo.mysql.JDBC_Select;

import java.sql.SQLException;
import java.util.List;

public class Order_Show {
    Order order;
    JDBC_Manager jdbc_manager;
    JDBC_Select jdbc_select;

    Order_Show(Order order)
        throws SQLException,ClassNotFoundException{
        this.order=order;
        jdbc_manager=new JDBC_Manager();
        jdbc_select=new JDBC_Select(jdbc_manager);
    }

    //通过某一个参数来查找并初始化某一指定表格内容
    public boolean init_specific_table(int type,String column,String value){
        List<String> ls=null;
        ls=jdbc_select.Query(type,order.getTable_name(),column,value);
        if(ls!=null){
            String[] values=new String[ls.size()];
            ls.toArray(values);
            return order.getOrder(values);
        }
        return false;
    }

    //通过某两个参数来查找并初始化某一指定表格内容
    public boolean init_specific_table(int type,String[] column,String[] value){
        List<String> ls=null;
        ls=jdbc_select.Query(type,order.getTable_name(),column,value);
        if(ls!=null){
            String[] values=new String[ls.size()];
            ls.toArray(values);
            return order.getOrder(values);
        }
        return false;
    }

    //通过一个参数查找并返回特定列的值
    public List<String> find_column(String s, int type, String column, String value){
        List<String> ls=jdbc_select.Query(s,type,order.getTable_name(),column,value);
        return ls;
    }

    //通过两个参数查找并返回特定列的值
    public List<String> find_column(String s,int type,String[] column,String[] value){
        List<String> ls=jdbc_select.Query(s,type,order.getTable_name(),column,value);
        return ls;
    }



}

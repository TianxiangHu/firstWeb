package com.example.firstdemo.product;

import com.example.firstdemo.mysql.JDBC_Manager;
import com.example.firstdemo.mysql.JDBC_Select;

import java.sql.SQLException;
import java.util.List;

/*
* 因为这个类的特殊操作——搜索，所以需要加一个可以根据条件单独返回表中某一列的内容的函数
* */
public class Commodity_Show {
    Commodity commodity;
    JDBC_Manager jdbc_manager;
    JDBC_Select jdbc_select;

    Commodity_Show(Commodity commodity)
        throws SQLException,ClassNotFoundException{
        this.commodity=commodity;
        jdbc_manager=new JDBC_Manager();
        jdbc_select=new JDBC_Select(jdbc_manager);
    }


    //通过某一个参数来查找并初始化某一指定表格内容
    public boolean init_specific_table(int type,String column,String value){
        List<String> ls=null;
        ls=jdbc_select.Query(type,commodity.getTable_name(),column,value);
        if(ls!=null){
            String[] values=new String[ls.size()];
            ls.toArray(values);
            return commodity.getCommodity(values);
        }
        return false;
    }

    //通过某两个参数来查找并初始化某一指定表格内容
    public boolean init_specific_table(int type,String[] column,String[] value){
        List<String> ls=null;
        ls=jdbc_select.Query(type,commodity.getTable_name(),column,value);
        if(ls!=null){
            String[] values=new String[ls.size()];
            ls.toArray(values);
            return commodity.getCommodity(values);
        }
        return false;
    }

    //通过一个参数查找并返回特定列的值
    public List<String> find_column(String s,int type,String column,String value){
        List<String> ls=jdbc_select.Query(s,type,commodity.getTable_name(),column,value);
        return ls;
    }

    //通过两个参数查找并返回特定列的值
    public List<String> find_column(String s,int type,String[] column,String[] value){
        List<String> ls=jdbc_select.Query(s,type,commodity.getTable_name(),column,value);
        return ls;
    }

    //一些特殊情况，直接传入sql式，比如使用正则表达式时
    public List<String> find_column(String s){
        List<String> ls=jdbc_select.Query(s);
        return ls;
    }


}

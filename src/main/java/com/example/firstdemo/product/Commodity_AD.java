package com.example.firstdemo.product;

import com.example.firstdemo.mysql.JDBC_Delete;
import com.example.firstdemo.mysql.JDBC_Insert;
import com.example.firstdemo.mysql.JDBC_Manager;

import java.sql.SQLException;

public class Commodity_AD {
    Commodity commodity;
    JDBC_Manager jdbc_manager;
    JDBC_Insert jdbc_insert;
    JDBC_Delete jdbc_delete;

    Commodity_AD(Commodity commodity)
        throws SQLException,ClassNotFoundException{
        this.commodity=commodity;
        jdbc_manager=new JDBC_Manager();
        jdbc_insert=new JDBC_Insert(jdbc_manager);
        jdbc_delete=new JDBC_Delete(jdbc_manager);
    }

    //支持一次插入表的一行数据
    public boolean add_Commodity(String[] columns,String[] values) {
        return jdbc_insert.Insert(commodity.getTable_name(),columns,values);
    }

    //删除某表中的数据,两个判断条件
    public boolean delete_Commodity(int type,String[] column_name,Object[] objects){
        return jdbc_delete.Delete(type,commodity.getTable_name(),column_name,objects);
    }

    //删除某表中的数据，一个判断条件
    public boolean delete_Commodity(int type,String column_name,Object objects){
        return jdbc_delete.Delete(type,commodity.getTable_name(),column_name,objects);
    }

    //删除某表中的所有数据
    public boolean delete_Commodity(int type){
        return jdbc_delete.Delete(type,commodity.getTable_name());
    }

}

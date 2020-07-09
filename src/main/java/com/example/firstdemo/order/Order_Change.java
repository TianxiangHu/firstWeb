package com.example.firstdemo.order;

import com.example.firstdemo.mysql.JDBC_Manager;
import com.example.firstdemo.mysql.JDBC_Update;

import java.sql.SQLException;

public class Order_Change {
    Order order;
    JDBC_Manager jdbc_manager;
    JDBC_Update jdbc_update;

    Order_Change(Order order)
            throws SQLException,ClassNotFoundException{
        this.order=order;
        jdbc_manager=new JDBC_Manager();
        jdbc_update=new JDBC_Update(jdbc_manager);
    }

    //一次改变一个特定数据
    public boolean change_one(String column,String condition,Object[] objects) {
        return jdbc_update.Update(order.getTable_name(),column,condition,objects);
    }

    //一次改变多个特定数据
    public boolean change_plurality(String[] columns,String condition,Object[] objects){
        for (int i=0;i<columns.length;i++){
            Object[] temp_objects=new Object[2];
            temp_objects[0]=objects[i*2];
            temp_objects[1]=objects[i*2+1];
            if(!jdbc_update.Update(order.getTable_name(),columns[i],condition,temp_objects))
                return false;
        }
        return true;
    }
}

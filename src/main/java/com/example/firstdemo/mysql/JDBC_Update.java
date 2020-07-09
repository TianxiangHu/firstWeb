package com.example.firstdemo.mysql;

import java.sql.PreparedStatement;

/*
* 只实现“UPDATE table_name SET ?=? WHERE ?=?"语句
* */
public class JDBC_Update {
    JDBC_Manager jdbc_manager;
    private PreparedStatement preparedStatement=null;

    public JDBC_Update(JDBC_Manager jdbc_manager){
        this.jdbc_manager=jdbc_manager;
    }

    public boolean Update(String table_name,String column,String condition,Object[] objects){
        try{
            String sql="UPDATE "+table_name+" SET "+column+"=? WHERE "+condition+"=?";
            preparedStatement=jdbc_manager.getConnection().prepareStatement(sql);
            //System.out.println(preparedStatement);
            for (int i=0;i<objects.length;i++){
                preparedStatement.setObject(i+1,objects[i]);
            }
            int re=preparedStatement.executeUpdate();
            return re>0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

}

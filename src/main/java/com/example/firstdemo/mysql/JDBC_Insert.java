package com.example.firstdemo.mysql;

import java.sql.PreparedStatement;

/*
* 只实现“INSERT INTO table() VALUES ()"语句
* */
public class JDBC_Insert {
    JDBC_Manager jdbc_manager;
    private PreparedStatement preparedStatement=null;

    public JDBC_Insert(JDBC_Manager jdbc_manager){
        this.jdbc_manager=jdbc_manager;
    }

    public boolean Insert(String table_name,String[] columns,String[] values){
        try{
            if(columns.length!=values.length)
                return false;
            String column="",value="";
            for (int i=0;i<columns.length-1;i++){
                column=column+columns[i]+",";
                value=value+"\""+values[i]+"\",";
            }
            column+=columns[columns.length-1];
            value+="\""+values[values.length-1]+"\"";
            String sql="INSERT INTO "+table_name+" ("+column+") VALUES ("+value+")";
            preparedStatement=jdbc_manager.getConnection().prepareStatement(sql);
            int re=preparedStatement.executeUpdate();
            return re>0 ? true:false;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}

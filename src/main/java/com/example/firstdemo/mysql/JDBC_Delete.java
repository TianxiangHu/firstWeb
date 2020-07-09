package com.example.firstdemo.mysql;

import java.sql.PreparedStatement;


public class JDBC_Delete {
    JDBC_Manager jdbc_manager;
    String delete_Base="DELETE FROM ";
    String[] deleteSql={
            " ",
            " WHERE $=?",
            " WHERE $>?",
            " WHERE $<?",
            " WHERE $!=?",
            " WHERE $=? AND $=?",
            " WHERE $=? OR $=?"
    };
    public static int DELETEALL=0;
    public static int DELETEEQUAL=1;
    public static int DELETEBIGGER=2;
    public static int DELETESMALLER=3;
    public static int DELETEUNEQUAL=4;
    public static int DELETEAND=5;
    public static int DELETEOR=6;
    private PreparedStatement preparedStatement=null;

    public JDBC_Delete(JDBC_Manager jdbc_manager){
        this.jdbc_manager=jdbc_manager;
    }

    public boolean Delete(int type,String table_name,String[] column_name,Object[] objects){
        try{
            //拼接处后半句sql语句
            String half_sql=deleteSql[type];
            String[] strs=half_sql.split("\\$");
            String hs="";
            for (int i=0;i<column_name.length;i++){
                hs=hs+strs[i]+column_name[i];
            }
            hs+=strs[strs.length-1];

            //完整的sql语句
            String sql=delete_Base+table_name+hs;

            preparedStatement=jdbc_manager.getConnection().prepareStatement(sql);
            for (int i=0;i<objects.length;i++){
                preparedStatement.setObject(i+1,objects[i]);
            }
            int re=preparedStatement.executeUpdate();
            return re>0 ? true:false;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean Delete(int type,String table_name,String column_name,Object objects){
        try{
            //拼接处后半句sql语句
            String half_sql=deleteSql[type];
            String[] strs=half_sql.split("\\$");
            String hs=strs[0]+column_name+strs[1];

            //完整的sql语句
            String sql=delete_Base+table_name+hs;

            preparedStatement=jdbc_manager.getConnection().prepareStatement(sql);
            preparedStatement.setObject(1,objects);
            int re=preparedStatement.executeUpdate();
            return re>0 ? true:false;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean Delete(int type,String table_name){
        try{
            String sql=delete_Base+table_name;
            preparedStatement=jdbc_manager.getConnection().prepareStatement(sql);
            int re=preparedStatement.executeUpdate();
            return re>0 ? true:false;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}

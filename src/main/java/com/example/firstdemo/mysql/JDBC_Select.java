package com.example.firstdemo.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

public class JDBC_Select {
    JDBC_Manager jdbc_manager;
    String select_Base="SELECT * FROM ";
    String[] selectSql={
            " ",
            " WHERE $=?",
            " WHERE $>?",
            " WHERE $<?",
            " WHERE $!=?",
            " WHERE $=? AND $=?",
            " WHERE $=? OR $=?"
    };
    public static int GETALL=0;//各种意义上它都用不到了
    public static int GETEQUAL=1;
    public static int GETBIGGER=2;
    public static int GETSMALLER=3;
    public static int GETUNEQUAL=4;
    public static int GETAND=5;
    public static int GETOR=6;
    private PreparedStatement preparedStatement=null;

    public JDBC_Select(JDBC_Manager jdbc_manager){
        this.jdbc_manager=jdbc_manager;
    }

    public List<String> Query(int type,String table_name,String[] column_name,Object[] objects){
        try{
            //拼接处后半句sql语句
            String half_sql=selectSql[type];
            String[] strs=half_sql.split("\\$");
            String hs="";
            for (int i=0;i<column_name.length;i++){
                hs=hs+strs[i]+column_name[i];
            }
            hs+=strs[strs.length-1];

            //完整的sql语句
            String sql=select_Base+table_name+hs;

            preparedStatement=jdbc_manager.getConnection().prepareStatement(sql);
            for (int i=0;i<objects.length;i++){
                preparedStatement.setObject(i+1,objects[i]);
            }
            return assist_re(preparedStatement);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<String> Query(int type,String table_name,String column_name,Object objects){
        try{
            //拼接处后半句sql语句
            String half_sql=selectSql[type];
            String[] strs=half_sql.split("\\$");
            String hs="";
            hs=hs+strs[0]+column_name+strs[strs.length-1];

            //完整的sql语句
            String sql=select_Base+table_name+hs;

            preparedStatement=jdbc_manager.getConnection().prepareStatement(sql);
            preparedStatement.setObject(1,objects);
            return assist_re(preparedStatement);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<String> Query(int type,String table_name){
        try{
            String sql=select_Base+table_name;
            preparedStatement=jdbc_manager.getConnection().prepareStatement(sql);
            return assist_re(preparedStatement);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //下面两个函数为返回特定列的符合条件的内容
    public List<String> Query(String special_column,int type,String table_name,String column_name,Object objects){
        try{
            //拼接处后半句sql语句
            String half_sql=selectSql[type];
            String[] strs=half_sql.split("\\$");
            String hs="";
            hs=hs+strs[0]+column_name+strs[strs.length-1];

            //完整的sql语句
            String sql="SELECT "+special_column+" FROM "+table_name+hs;

            preparedStatement=jdbc_manager.getConnection().prepareStatement(sql);
            preparedStatement.setObject(1,objects);
            return assist_re(preparedStatement);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<String> Query(String special_column,int type,String table_name,String[] column_name,Object[] objects){
        try{
            //拼接处后半句sql语句
            String half_sql=selectSql[type];
            String[] strs=half_sql.split("\\$");
            String hs="";
            for (int i=0;i<column_name.length;i++){
                hs=hs+strs[i]+column_name[i];
            }
            hs+=strs[strs.length-1];

            //完整的sql语句
            String sql="SELECT "+special_column+" FROM "+table_name+hs;

            preparedStatement=jdbc_manager.getConnection().prepareStatement(sql);
            for (int i=0;i<objects.length;i++){
                preparedStatement.setObject(i+1,objects[i]);
            }
            return assist_re(preparedStatement);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //一些特殊情况，直接传入sql式
    public List<String> Query(String mysql) {
        try {
            preparedStatement = jdbc_manager.getConnection().prepareStatement(mysql);
            return assist_re(preparedStatement);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private List<String> assist_re(PreparedStatement preparedStatement){
        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            List<String> ls = new ArrayList<>();
            boolean flag=false;
            while (resultSet.next()) {
                for (int i = 0; i < resultSetMetaData.getColumnCount(); i++) {
                    ls.add(resultSet.getString(i + 1));
                }
                flag=true;
            }
            if(flag)
                return ls;
            else
                return null;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

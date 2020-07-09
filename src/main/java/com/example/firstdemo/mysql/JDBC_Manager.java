package com.example.firstdemo.mysql;

import java.sql.*;
import java.util.List;
import java.util.ResourceBundle;

import static com.example.firstdemo.mysql.JDBC_Delete.*;

public class JDBC_Manager {
    private static String DRIVER="";
    private static String SeverUrl="";
    private static String User="";
    private static String Password="";
    private Connection connection=null;
    private Statement statement=null;//最好不使用Statement，效率低且会受SQL注入攻击
    private PreparedStatement preparedStatement=null;//推荐使用

    static{
        ResourceBundle bundle=ResourceBundle.getBundle("db");
        DRIVER=bundle.getString("driver");
        SeverUrl=bundle.getString("url");
        User=bundle.getString("username");
        Password=bundle.getString("password");
    }

    public JDBC_Manager()
            throws ClassNotFoundException, SQLException {
        Driver.class.forName(DRIVER);
        connection= DriverManager.getConnection(SeverUrl,User,Password);
    }

    //测试函数
    public void InsertIntoMySQL()
            throws SQLException{
        String sql = "INSERT INTO first(title) VALUES (?)";
        preparedStatement=connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
        for (int i=1;i<3;i++) {
            preparedStatement.setString(1, String.valueOf(i));
            preparedStatement.addBatch();
        }
        preparedStatement.executeBatch();
        preparedStatement.clearBatch();

        //即使是进行批处理，获取的第一个主键也是进行的第一个操作对应主键
        ResultSet resultSet=preparedStatement.getGeneratedKeys();
        if(resultSet.next()){
            int id=resultSet.getInt(1);
            System.out.println("获取的id："+id);
        }

        preparedStatement.close();
    }

    /*这个是很好用啦，但是应该怎么用啊……再想想*/
    //我们发现，增删改只有SQL语句和传入的参数是不知道的而已，所以让调用该方法的人传递进来
    //由于传递进来的参数是各种类型的，而且数目是不确定的，所以使用Object[]
    public void update(String sql, Object[] objects) {
        try {
            preparedStatement = connection.prepareStatement(sql);
            //根据传递进来的参数，设置SQL占位符的值
            for (int i = 0; i < objects.length; i++) {
                preparedStatement.setObject(i + 1, objects[i]);
            }
            //执行SQL语句
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //如何封装一个查询的函数也是和上面update函数一样的流程

    protected void close()throws SQLException{
        if(connection!=null)
            connection.close();
    }

    //忘记close了就指望销毁的时候关闭
    protected void finalize()
            throws SQLException{
        if(connection!=null)
            connection.close();
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public static void main(String[] args){
        try {
            JDBC_Manager jdbc_manager = new JDBC_Manager();
            JDBC_Update jdbc_update=new JDBC_Update(jdbc_manager);
            String table_name="role",column="role_name",condition="role_id";
            String[] values={"用户","2"};
            jdbc_update.Update(table_name,column,condition,values);

            jdbc_manager.close();
        }catch  (ClassNotFoundException e){
            e.printStackTrace();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

}

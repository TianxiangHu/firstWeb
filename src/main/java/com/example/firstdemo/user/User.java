package com.example.firstdemo.user;
/*
* 这个包用于封装和用户（User）有关的操作，具体有：
*   1，获取用户信息
*   2，修改用户信息
*   3，增加用户
*   4，删除用户
*   5,用户收货地址管理（表名为user_adr，新增，相关函数还未处理）
* 实现四个辅助类，User类为基础类
* */

import java.sql.SQLException;
import java.util.List;

import static com.example.firstdemo.mysql.JDBC_Select.*;

public class User {
    private int user_id,role_id;//用户id和角色id
    private String role_name,role_sign,role_remark,name,password,user_name,pic_img_adr;//账号名；账号密码；昵称；头像照片储存地址
    private String email,contact,create_time;//邮箱；联系方式；创建时间
    private int role_status,user_status;//角色状态，0=冻结/1=正常/2=管理员；用户状态，0=冻结/1=正常
    private List<String> adr;//用户的地址，可能有多个
    private final String table_role="role",table_user_info="user_info",table_user_role="user_role",table_adr="user_adr";

    public Delete_User delete_user;
    public Add_User add_user;
    public Change_User_Info change_user_info;
    public Obtain_User_info obtain_user_info;

    public User()
        throws SQLException,ClassNotFoundException {
        delete_user=new Delete_User(this);
        add_user=new Add_User(this);
        change_user_info=new Change_User_Info(this);
        obtain_user_info=new Obtain_User_info(this);
    }

    //用来填写role表中一行相关变量
    public boolean getRole(String[] strs) {
        if (strs.length == 5) {
            setRole_id(Integer.valueOf(strs[0]));
            setRole_name(strs[1]);
            setRole_sign(strs[2]);
            setRole_status(Integer.valueOf(strs[3]));
            setRole_remark(strs[4]);
            return true;
        }
        else
            return false;//说明给的参数不是role表的一行全部内容
    }

    //用来填写user_info表中一行相关变量
    public boolean getUserInfo(String[] strs) {
        if (strs.length == 9) {
            setUser_id(Integer.valueOf(strs[0]));
            setName(strs[1]);
            setPassword(strs[2]);
            setUser_name(strs[3]);
            setPic_img_adr(strs[4]);
            setUser_status(Integer.valueOf(strs[5]));
            setEmail(strs[6]);
            setContact(strs[7]);
            setCreate_time(strs[8]);
            return true;
        }
        else
            return false;//说明给的参数不是role表的一行全部内容
    }

    //用来填写user_role表中一行相关变量，实际上查这个表就是根据user_id查role_id
    public boolean getUser_Role(String[] strs) {
        if (strs.length == 3) {
            setRole_id(Integer.valueOf(strs[2]));
            return true;
        }
        else
            return false;//说明给的参数不是role表的一行全部内容
    }

    //测试用
    public static void main(String[] args)
        throws SQLException,ClassNotFoundException{
        User user=new User();
        String table_name="user_info";
        String column="name",value="hu";
        if(user.obtain_user_info.init_specific_table(GETEQUAL,table_name,column,value)){
            System.out.println(user.getPassword());
        }
    }


    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPic_img_adr() {
        return pic_img_adr;
    }

    public void setPic_img_adr(String pic_img_adr) {
        this.pic_img_adr = pic_img_adr;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getRole_status() {
        return role_status;
    }

    public void setRole_status(int role_status) {
        this.role_status = role_status;
    }

    public int getUser_status() {
        return user_status;
    }

    public void setUser_status(int user_status) {
        this.user_status = user_status;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    public String getRole_sign() {
        return role_sign;
    }

    public void setRole_sign(String role_sign) {
        this.role_sign = role_sign;
    }

    public String getRole_remark() {
        return role_remark;
    }

    public void setRole_remark(String role_remark) {
        this.role_remark = role_remark;
    }

    public List<String> getAdr() {
        return adr;
    }

    public void setAdr(List<String> adr) {
        this.adr = adr;
    }

    public String getTable_role() {
        return table_role;
    }

    public String getTable_user_info() {
        return table_user_info;
    }

    public String getTable_user_role() {
        return table_user_role;
    }

    public String getTable_adr() {
        return table_adr;
    }
}



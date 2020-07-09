package com.example.firstdemo.controller;

import com.example.firstdemo.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.firstdemo.mysql.JDBC_Select.*;

@Controller
public class RegisterController {
    User user;
    String[] columns={
            "name",
            "password",
            "user_name",
            "email",
            "contact"
    };
    String[] ur_columns={
            "user_id",
            "role_id"
    };
    String user_info="user_info";
    String user_role="user_role";
    String name_error="错误，用户名为空或用户名过长（大于10位）";
    String user_name_error="错误，昵称为空或昵称过长（大于10位）";
    String password_error="错误，密码过长或过短（小于6位或大于二十位）";
    String repeat_password_error="错误，两次密码输入不同";
    String email_error="错误，邮箱为空或格式非法";
    String name_error2="错误，用户名已经存在";

    RegisterController()
        throws SQLException,ClassNotFoundException{
        user=new User();
    }

    //用来重置的函数，避免之前的错误被再次错误地呈现
    private void init_register(Model model){
        model.addAttribute("name_error", "");
        model.addAttribute("user_name_error","");
        model.addAttribute("password_error","");
        model.addAttribute("repeat_password_error", "");
        model.addAttribute("email_error","");
    }

    //该函数用来检测邮箱格式正确与否
    private boolean check_email_format(String email){
        if(email==null)
            return false;
        String check="^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        //String check2="^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
        Pattern regex=Pattern.compile(check);
        Matcher matcher=regex.matcher(email);
        return matcher.matches();
    }

    //简化代码的函数
    private void check_others(String name, String value, Model model,boolean flag){
        if(name.compareTo("password")==0){
            System.out.println("Here 1");
            if(value!=null){
                if((value.length()>=6)&&(value.length()<=20)) {
                    System.out.println("value"+value);
                    System.out.println(value.length());
                    return;
                }
            }
            model.addAttribute("password_error",password_error);
            flag=false;
        }
        else {
            if (value != null) {
                if (value.length() <= 10)
                    return;
            }
            if (name.compareTo("name") == 0) {
                model.addAttribute("name_error", name_error);
                flag = false;
            } else {
                model.addAttribute("user_name_error", user_name_error);
                flag=false;
            }
        }
    }

    @RequestMapping("register")
    public String register(){
        return "register";
    }

    //这个要做的事好多啊，不仅要检查格式，还要检查用户名是否已经存在。
    //注册功能增加的用户只可以是普通用户，所以role角色硬编码在函数中
    @RequestMapping("Register")
    public String Register(HttpServletRequest httpServletRequest,Model model) {
        String name = httpServletRequest.getParameter("name");
        String user_name = httpServletRequest.getParameter("user_name");
        String password = httpServletRequest.getParameter("password");
        String repeat_password = httpServletRequest.getParameter("repeat password");
        String email = httpServletRequest.getParameter("email");
        String contact=httpServletRequest.getParameter("contact");

        System.out.println(name);
        System.out.println(password);

        boolean flag = true;

        init_register(model);

        if ((password!=null)&&(repeat_password.compareTo(password) != 0)) {
            model.addAttribute("repeat_password_error", repeat_password_error);
            flag = false;
        }
        if (!check_email_format(email)) {
            model.addAttribute("email_error", email_error);
            flag = false;
        }
        check_others("name", name, model, flag);
        check_others("user_name", user_name, model, flag);
        check_others("password", password, model, flag);

        if (!flag) {
            //如果下述条件为真，则说明这是第一次进入register页面，不应该报错
            System.out.println("Here 2");
            if((name==null)&&(user_name==null)&&(password==null)&&(repeat_password==null)&&(email==null))
                init_register(model);
            return "register";
        }
        else {
            if(user.obtain_user_info.init_specific_table(GETEQUAL,user_info,"name",name)) {
                model.addAttribute("name_error",name_error2);
                return "register";
            }
            String[] values=new String[5];
            values[0]=name;values[1]=password;values[2]=user_name;values[3]=email;values[4]=contact;
            user.add_user.add_User_Info(user_info,columns,values);
            user.obtain_user_info.init_specific_table(GETEQUAL,user_info,"name",name);
            String[] us_values={String.valueOf(user.getUser_id()),"2"};
            user.add_user.add_User_Info(user_role,ur_columns,us_values);
            return "redirect:/hello";
        }
    }

    @RequestMapping("GoRegister")
    public String GoRegister(){
        return "redirect:/register";
    }

}

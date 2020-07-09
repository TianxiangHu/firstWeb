package com.example.firstdemo.controller;

import com.example.firstdemo.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import static com.example.firstdemo.mysql.JDBC_Select.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;

/*
* 包含“hello”，“register”
* */

@Controller
public class HelloController {
    User user;

    HelloController()
        throws SQLException,ClassNotFoundException {
        user=new User();
    }

    @RequestMapping("/hello")
    public String hello(HttpServletRequest httpServletRequest, HttpSession session){
        String name=httpServletRequest.getParameter("name");
        String password=httpServletRequest.getParameter("password");
        System.out.println(name);
        System.out.println(password);
        if(name!=null) {
            user.obtain_user_info.init_specific_table(GETEQUAL, "user_info", "name", name);
            if (password.compareTo(user.getPassword()) == 0) {
                session.setAttribute("user name",user.getName());
                session.setAttribute("user id",user.getUser_id());
                return "redirect:/anonymous/index";
            }
        }
        return "hello";
    }

}
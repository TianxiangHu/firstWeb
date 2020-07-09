package com.example.firstdemo.controller;

import com.example.firstdemo.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;

/*
还没写好，暂时放这
*/

@Controller
public class User_infoController {
    User user;

    User_infoController()
        throws SQLException,ClassNotFoundException{
        user=new User();
    }

    @RequestMapping("anonymous/user_info")
    public String user_info(HttpSession session){
        if(session.getAttribute("user id")==null)
            return "redirect:/hello";
        return "anonymous/user_info";
    }

    @RequestMapping("GoUser_info")
    public String GoUser_info(){
        return "redirect:anonymous/user_info";
    }

}

package com.example.firstdemo.controller;

import com.example.firstdemo.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;

/*
 * 包含“index”，“search”，“user_info”
 * */

@Controller
public class IndexController {
    User user;

    IndexController()
            throws SQLException,ClassNotFoundException {
        user=new User();
    }

    @RequestMapping("anonymous/index")
    public String index(Model model,HttpSession session){
        if(session.getAttribute("user id")==null)
            return "redirect:/hello";
        String name=(String)session.getAttribute("user name");
        model.addAttribute("name",name);
        return "/anonymous/index";
    }

}

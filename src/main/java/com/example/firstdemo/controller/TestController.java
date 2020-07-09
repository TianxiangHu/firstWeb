package com.example.firstdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {
    @RequestMapping("/test")
    public String test(){
        return "test";
    }

    @RequestMapping("/test/upload")
    public String upload(Model model){
        model.addAttribute("updateResult","已经成功完成测试！");
        return "test";
    }

}

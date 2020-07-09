package com.example.firstdemo;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class image_configuration implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        System.out.println("配置文件已经生效");
        String path="E:\\2019-2020 second term\\网络应用开发技术\\作业\\我的项目\\firstdemo\\src\\main\\resources\\static\\image\\";
        registry.addResourceHandler("/image/**").addResourceLocations("file:"+path);
    }
}
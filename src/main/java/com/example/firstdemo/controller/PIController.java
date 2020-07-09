package com.example.firstdemo.controller;

/*
还没写好，暂时放这
个人信息页面相关的控制逻辑
*/

import com.example.firstdemo.order.Order;
import com.example.firstdemo.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

import static com.example.firstdemo.mysql.JDBC_Select.GETEQUAL;

@Controller
public class PIController {
    User user;
    int user_id=-1;
    String[] adr_columns={
            "user_id",
            "adr"
    };
    String[] user_info_columns={
            "user_name",
            "password",
            "pic_img_adr",
            "email",
            "contact"
    };

    PIController()
            throws SQLException,ClassNotFoundException{
        user=new User();
    }

    @RequestMapping("anonymous/user_info/personal_information")
    public String personal_information(HttpSession session, Model model){
        if(session.getAttribute("user id")==null)
            return "redirect:/hello";
        user_id=(int)session.getAttribute("user id");
        user.obtain_user_info.init_specific_table(GETEQUAL,"user_info","user_id",String.valueOf(user_id));
        init_user_info(model);

        System.out.println("Here 2");

        return "anonymous/user_info/personal_information";
    }

    @RequestMapping("/anonymous/user_info/upload_user_info")
    public String upload_user_info(@RequestParam(value = "pic_img_change") MultipartFile file, HttpServletRequest httpServletRequest, Model model){
        System.out.println("Here 1");
        String user_name = httpServletRequest.getParameter("user_name_change");
        String password = httpServletRequest.getParameter("password_change");
        String email = httpServletRequest.getParameter("email_change");
        String contact=httpServletRequest.getParameter("contact_change");
        String adr=httpServletRequest.getParameter("adr_add");

        String filename;
        String access_name=null;
        if(file.isEmpty()){
            filename=null;
            System.out.println("文件为空。");
        }
        else{
            filename=file.getOriginalFilename();
            String suffixName = filename.substring(filename.lastIndexOf("."));  // 后缀名
            String filePath = "E:\\2019-2020 second term\\网络应用开发技术\\作业\\我的项目\\firstdemo\\src\\main\\resources\\static\\image\\user\\"; // 上传后的路径
            filename= UUID.randomUUID()+suffixName;
            access_name="../image/user/"+filename;
            File dest=new File(filePath+filename);
            try{
                file.transferTo(dest);
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        if(adr!=null&&adr!=""){
            String[] adr_values={String.valueOf(user.getUser_id()),adr};
            user.add_user.add_User_Info(user.getTable_adr(),adr_columns,adr_values);
        }

        String[] user_info_values=new String[user_info_columns.length];
        user_info_values[0]=user_name;user_info_values[1]=password;user_info_values[2]=access_name;user_info_values[3]=email;user_info_values[4]=contact;
        for (int i=0;i<user_info_columns.length;i++){
            if(user_info_values[i]!=null&&user_info_values[i].compareTo("")!=0){
                String[] values={user_info_values[i],String.valueOf(user.getUser_id())};
                user.change_user_info.change_one("user_info",user_info_columns[i],"user_id",values);
            }
        }

        user.obtain_user_info.init_specific_table(GETEQUAL,"user_info","user_id",String.valueOf(user.getUser_id()));
        init_user_info(model);

        return "anonymous/user_info/personal_information";
    }

    @RequestMapping("GoPersonal_Information")
    public String GoPending_Order(){
        return "redirect:/anonymous/user_info/personal_information";
    }

    private void init_user_info(Model model){
        model.addAttribute("name",user.getName());
        model.addAttribute("user_name",user.getUser_name());
        String temp="../"+user.getPic_img_adr();
        System.out.println("pic adr: "+temp);
        model.addAttribute("image_adr",temp);
        model.addAttribute("status",user.getUser_status());
        model.addAttribute("email",user.getEmail());
        model.addAttribute("contact",user.getContact());
    }
}

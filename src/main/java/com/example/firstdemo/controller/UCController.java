package com.example.firstdemo.controller;

import com.example.firstdemo.product.Commodity;
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

@Controller
public class UCController {
    Commodity commodity;

    String Commodity_table_name = "commodity";
    int user_id;
    final String[] columns = {
            "user_id",
            "commodity_name",
            "price",
            "introduction",
            "exist_number",
            "pic_img_adr",
            "remarks"
    };
    String[] values = new String[columns.length];
    String[] errors_name = {
            "commodity_error",
            "price_error",
            "introduction_error",
            "number_error",
            "picture_error"
    };
    String[] errors = {
            "请填写商品名",
            "请填写大于零的有两位小数的价格值",
            "请输入商品介绍，不多于两百字",
            "请填写大于零的数量值",
            "请上传图片"
    };


    UCController()
            throws SQLException, ClassNotFoundException {
        commodity = new Commodity();
    }

    @RequestMapping("anonymous/user_info/upload_commodity")
    public String UC(Model model,HttpSession session){
        if(session.getAttribute("user id")==null)
            return "redirect:/hello";
        model.addAttribute("visible7",true);
        init_UC(model);
        user_id=(int)session.getAttribute("user id");
        if(user_id<0)
            return "hello";
        return "anonymous/user_info/upload_commodity";
    }

    @RequestMapping("/upload_commodity")
    public String upload_commodity(@RequestParam(value = "picture") MultipartFile file, HttpSession session, HttpServletRequest httpServletRequest, Model model) {
        String filename;
        String access_name=null;
        if(file.isEmpty()){
            filename=null;
            System.out.println("文件为空。");
        }
        else{
            filename=file.getOriginalFilename();
            String suffixName = filename.substring(filename.lastIndexOf("."));  // 后缀名
            String filePath = "E:\\2019-2020 second term\\网络应用开发技术\\作业\\我的项目\\firstdemo\\src\\main\\resources\\static\\image\\product\\"; // 上传后的路径
            filename= UUID.randomUUID()+suffixName;
            access_name="../image/product/"+filename;
            System.out.println(filePath+filename);
            File dest=new File(filePath+filename);
            try{
                file.transferTo(dest);
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        //初始化不可见
        model.addAttribute("visible7",true);

        String[] strs=new String[errors_name.length+1];
        boolean flag=true;
        for (int i=0;i<errors_name.length-1;i++){
            strs[i]=httpServletRequest.getParameter(columns[i+1]);
            if(flag)
                flag=check(model,strs[i],i);
        }
        strs[errors_name.length-1]=access_name;
        if(flag)
            flag=check(model,strs[errors_name.length-1],errors_name.length-1);
        //备注信息是可以不填的，所以不检测
        strs[errors_name.length]=httpServletRequest.getParameter("remarks");

        if(!flag){
            for (int i=0;i<strs.length;i++){
                if(strs[i]==null||strs[i].compareTo("")==0)
                    continue;
                else
                    return "/anonymous/user_info/upload_commodity";
            }
            init_UC(model);
            return "/anonymous/user_info/upload_commodity";
        }

        values[0]=String.valueOf(user_id);
        for (int i=1;i<columns.length;i++){
            values[i]=strs[i-1];
        }
        //添加商品成功则显示
        flag=commodity.commodity_ad.add_Commodity(columns,values);
        model.addAttribute("visible7",!flag);

        return "/anonymous/user_info/upload_commodity";
    }

    @RequestMapping("GoUpload_Commodity")
    public String GoUpload_Commodity() {
        return "redirect:/anonymous/user_info/upload_commodity";
    }

    //用来重置的函数，避免之前的错误被再次错误地呈现
    private void init_UC(Model model) {
        model.addAttribute("commodity_error", "");
        model.addAttribute("price_error", "");
        model.addAttribute("introduction_error", "");
        model.addAttribute("number_error", "");
        model.addAttribute("picture_error", "");
    }

    //用来检查合法性的函数
    private boolean check(Model model, String s, int type) {
        if (s == null||s.compareTo("")==0) {
            model.addAttribute(errors_name[type], errors[type]);
            return false;
        }
        if (type == 1) {
            if (Float.valueOf(s) <= 0) {
                model.addAttribute(errors_name[type], errors[type]);
                return false;
            }
        }
        if (type == 3) {
            if (Integer.valueOf(s) <= 0) {
                model.addAttribute(errors_name[type], errors[type]);
                return false;
            }
        }
        return true;
    }
}

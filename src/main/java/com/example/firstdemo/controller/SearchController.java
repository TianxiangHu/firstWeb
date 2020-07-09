package com.example.firstdemo.controller;

import com.example.firstdemo.order.Order;
import com.example.firstdemo.product.Commodity;
import com.example.firstdemo.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.example.firstdemo.mysql.JDBC_Select.GETEQUAL;

/*
* 还没写好，暂时放这
* */


@Controller
public class SearchController {
    int user_id=-1;
    String visible="visible";
    String[] columns={
            "name",
            "image_adr",
            "seller",
            "price",
            "exist",
            "current_number",
            "introduction"
    };
    String[] order_columns={
            "commodity_id",
            "buy_user_id",
            "status",
            "create_time",
            "price",
            "exist_number"
    };
    //特殊的sql语句
    final String regex_sql="select commodity_name from commodity where commodity_name regexp ";
    final String number_error_value="应填入大于0小于该商品现存数量的数值！";
    final String number_error="number_error";

    final int column_number=6;
    final int commodity_number=5;
    int current_commodities_number=0;//记录当前显示的第一个商品在re中的序号
    List<String> re;//储存在MySQL中查询得到的多个name值

    Commodity commodity;
    User user;
    Order order;

    SearchController()
        throws SQLException,ClassNotFoundException{
        commodity=new Commodity();
        user=new User();
        order=new Order();
    }

    @RequestMapping("anonymous/search")
    public String init_search(HttpSession session,Model model){
        if(session.getAttribute("user id")==null)
            return "redirect:/hello";
        user_id=(int)session.getAttribute("user id");
        //设置表格为不可见
        for (int i=0;i<column_number;i++)
            init_one_visible(i,false,model);
        init_one_visible(6,false,model);
        init_one_visible(7,false,model);
        return "/anonymous/search";
    }

    @RequestMapping("GoSearch")
    public String GoSearch(){
        return "redirect:/anonymous/search";
    }

    @RequestMapping("anonymous/search_product")
    public String search(HttpServletRequest httpServletRequest, Model model){
        String search_name=httpServletRequest.getParameter("url");
        if(search_name!=null) {
            String regex = regex_sql + "\"" + search_name + "\"";
            System.out.println(regex);
            re = commodity.commodity_show.find_column(regex);
            System.out.println(re);
            for (int i=0;i<column_number;i++)
                init_one_visible(i,false,model);
            init_one_visible(6,false,model);
            init_one_visible(7,false,model);
            init_one_visible(5, true, model);
            if (re != null) {
                current_commodities_number=0;
                show_one_page(model,current_commodities_number);
            }
        }
        return "/anonymous/search";
    }

    @RequestMapping("anonymous/search_TurnDown")
    public String turnDown(Model model){
        if(re.size()-current_commodities_number>5){
            current_commodities_number+=5;
            show_one_page(model,current_commodities_number);
        }
        return "/anonymous/search";
    }

    @RequestMapping("anonymous/search_TurnUp")
    public String turnUp(Model model) {
        if (current_commodities_number >= 5) {
            current_commodities_number -= 5;
            show_one_page(model,current_commodities_number);
        }
        return "/anonymous/search";
    }

    @RequestMapping("anonymous/add_to_shopping_cart")
    public String add_to_shopping_cart(HttpServletRequest httpServletRequest,Model model){
        String buy_number="buy_number",error_number="error_number",add="add";
        for (int i=0;i<commodity_number;i++){
            if(httpServletRequest.getParameter(add+i)!=null&&httpServletRequest.getParameter(buy_number+i)!=null){
                int number=Integer.valueOf(httpServletRequest.getParameter(buy_number+i));
                if(number<=0||number>commodity.getExist_number()){
                    model.addAttribute(number_error+i,number_error_value);
                }
                else {
                    String[] order_values=new String[order_columns.length];
                    commodity.commodity_show.init_specific_table(GETEQUAL,"commodity_name",re.get(current_commodities_number+i));
                    order_values[0]=String.valueOf(commodity.getCommodity_id());
                    order_values[1]=String.valueOf(user_id);
                    order_values[2]="0";
                    Date d = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    order_values[3]=sdf.format(d);
                    order_values[4]=String.valueOf(commodity.getPrice() * number);
                    order_values[5]=String.valueOf(number);

                    order.order_ad.add_Commodity(order_columns,order_values);
                }
            }
        }

        show_one_page(model,current_commodities_number);
        return "anonymous/search";
    }

    //显示一页的商品信息，从re的第start个元素开始显示
    private void show_one_page(Model model,int start){
        //为了消除之前的影响，首先将除第一行外的所有行均设为不可见
        for (int i=0;i<commodity_number;i++){
            init_one_visible(i,false,model);
        }
        int current_page_number=re.size()-start;
        System.out.println(re.size());
        System.out.println(current_page_number);
        current_page_number=current_page_number>5 ? 5:current_page_number;
        for (int i=0;i<current_page_number;i++){
            commodity.commodity_show.init_specific_table(GETEQUAL,"commodity_name",re.get(start+i));
            init_one_visible(i,true,model);
            init_one_row(i,model);
        }
        if(current_commodities_number>=5)
            init_one_visible(6,true,model);
        else
            init_one_visible(6,false,model);

        if(re.size()-current_commodities_number>5)
            init_one_visible(7,true,model);
        else
            init_one_visible(7,false,model);
    }

    //设置搜索表格是否可见
    private void init_one_visible(int i, boolean flag, Model model){
        String current_visible=visible+i;
        if(flag)
            model.addAttribute(current_visible,false);
        else
            model.addAttribute(current_visible,true);
    }

    //设置一行商品信息
    private void init_one_row(int i,Model model){
        user.obtain_user_info.init_specific_table(GETEQUAL,"user_info","user_id",String.valueOf(commodity.getUser_id()));

        String[] values=new String[columns.length];
        values[0]=commodity.getCommodity_name();
        values[1]=commodity.getPic_img_adr();
        values[2]=user.getName();
        values[3]=String.valueOf(commodity.getPrice());
        values[4]=commodity.getStatus()==1 ? "有货":"无货";
        values[5]=String.valueOf(commodity.getExist_number());
        values[6]=commodity.getIntroduction();

        String temp;
        for (int j=0;j<columns.length;j++){
            temp=columns[j]+i;
            model.addAttribute(temp,values[j]);
        }
    }

}

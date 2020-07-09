package com.example.firstdemo.controller;

import com.example.firstdemo.order.Order;
import com.example.firstdemo.product.Commodity;
import com.example.firstdemo.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;

import static com.example.firstdemo.mysql.JDBC_Delete.DELETEEQUAL;
import static com.example.firstdemo.mysql.JDBC_Select.GETEQUAL;

@Controller
public class PCController {
    Order order;
    Commodity commodity;
    User user;

    final String confirm="confirm";
    int user_id = -1;
    int current_number=0;
    int current_adr_number=0;//记录当前显示的地址在re中的序列号
    int current_commodities_number = 0;//记录当前显示的第一个商品在re中的序号
    String visible="visible";
    List<String> re;//存储该用户的所有地址
    List<String > order_id_re;//存储
    String[] columns={
            "commodity_name",
            "image_adr",
            "commodity_seller",
            "commodity_number",
            "commodity_price",
            "commodity_introduction"
    };

    PCController()
            throws SQLException, ClassNotFoundException {
        order = new Order();
        commodity=new Commodity();
        user=new User();
    }

    @RequestMapping("/anonymous/user_info/purchase_confirm")
    public String pruchase_confirm(HttpServletRequest httpServletRequest, HttpSession session, Model model) {
        if (session.getAttribute("user id") == null)
            return "redirect:/hello";
        user_id = (int) session.getAttribute("user id");
        user.obtain_user_info.init_adr(String.valueOf(user_id));
        String name = (String) session.getAttribute("user name");
        model.addAttribute("name", name);
        boolean flag=false;
        for (int i=0;i<5;i++){
            if(httpServletRequest.getParameter(confirm+i)!=null&&session.getAttribute("shopping cart order id list")!=null){
                flag=true;
                break;
            }
        }
        if(flag)
            return "/anonymous/user_info/purchase_confirm";
        else
            return "redirect:/hello";
    }
    
    @RequestMapping("/anonymous/user_info/confirm_pay")
    public String confirm_pay(HttpSession session,HttpServletRequest httpServletRequest,Model model){
        String name=(String)session.getAttribute("user name");
        model.addAttribute("name",name);
        user_id = (int) session.getAttribute("user id");
        user.obtain_user_info.init_adr(String.valueOf(user_id));
        System.out.println("user id:"+user_id);
        boolean flag=false;
        for (int i=0;i<5;i++){
            if(httpServletRequest.getParameter(confirm+i)!=null&&session.getAttribute("shopping cart order id list")!=null){
                current_number=i;
                order_id_re=(List<String>)session.getAttribute("shopping cart order id list");
                current_commodities_number=(Integer)session.getAttribute("shopping cart current_commodities_number");
                flag=true;
                break;
            }
        }
        if(flag) {
            String order_id = order_id_re.get(current_commodities_number + current_number);
            order.order_show.init_specific_table(GETEQUAL, "order_id", order_id);
            commodity.commodity_show.init_specific_table(GETEQUAL, "commodity_id", String.valueOf(order.getCommodity_id()));
            init_one_row(model);
            re=user.getAdr();
            System.out.println(re);
            if(re!=null){
                current_adr_number=0;
                model.addAttribute("adr",re.get(current_adr_number));
                //先设置为不可见
                for (int i=1;i<3;i++){
                    init_one_visible(i,false,model);
                }
                if(re.size()-1>current_adr_number)
                    init_one_visible(1,true,model);
                if(current_adr_number>0)
                    init_one_visible(2,true,model);
            }

            return "/anonymous/user_info/purchase_confirm";
        }
        else
            return "/error";
    }

    //转到上一个地址
    @RequestMapping("/anonymous/user_info/previous")
    public String previous(Model model){
        if(current_adr_number>0){
            current_adr_number-=1;
            model.addAttribute("adr",re.get(current_adr_number));
        }
        return "/anonymous/user_info/purchase_confirm";
    }

    //转到下一个地址
    @RequestMapping("/anonymous/user_info/next")
    public String next(Model model){
        if(re.size()-1>current_adr_number){
            current_adr_number+=1;
            model.addAttribute("adr",re.get(current_adr_number));
        }
        return "/anonymous/user_info/purchase_confirm";
    }

    @RequestMapping("/anonymous/user_info/pay_it")
    @ResponseBody
    public String pay_it() {
        String[] values = {"1", String.valueOf(order.getOrder_id())};
        String order_column = "status", order_condition = "order_id";
        //付款成功，对应的商品的数量应该发生变化
        int buy_number = order.getExist_number();
        int commodity_exist_number = commodity.getExist_number();
        if (buy_number <= commodity_exist_number) {
            if(buy_number<commodity_exist_number){
                String[] values2={String.valueOf(commodity_exist_number-buy_number),String.valueOf(commodity.getCommodity_id())};
                String commodity_column="exist_number",commodity_condition="commodity_id";
                if(commodity.commodity_change.change_one(commodity_column,commodity_condition,values2)){
                    if (order.order_change.change_one(order_column, order_condition, values)) {
                        return "这是付款成功界面!\t因为没有做付款功能，所以留下了接口，出现此界面则说明已经付款成功，请在“待确定订单”界面查看";
                    }
                }
            }
            else{
                if(commodity.commodity_ad.delete_Commodity(DELETEEQUAL,"commodity_id",commodity.getCommodity_id())){
                    if (order.order_change.change_one(order_column, order_condition, values)) {
                        return "这是付款成功界面!\t因为没有做付款功能，所以留下了接口，出现此界面则说明已经付款成功，请在“待确定订单”界面查看";
                    }
                }
            }
            return "付款失败，未知错误！";
        }
        else
            return "付款失败，现有该商品数量不足";
    }

    //设置搜索表格是否可见
    private void init_one_visible(int i, boolean flag, Model model){
        String current_visible=visible+i;
        if(flag)
            model.addAttribute(current_visible,false);
        else
            model.addAttribute(current_visible,true);
    }

    //设置一行商品信息，因为只有一行所以i恒等于0
    private void init_one_row(Model model){
        user.obtain_user_info.init_specific_table(GETEQUAL,"user_info","user_id",String.valueOf(commodity.getUser_id()));

        String[] values=new String[columns.length];
        values[0]=commodity.getCommodity_name();
        values[1]="../"+commodity.getPic_img_adr();
        values[2]=user.getName();
        values[3]=String.valueOf(order.getExist_number());
        values[4]=String.valueOf(order.getPrice());
        values[5]=commodity.getIntroduction();

        for (int j=0;j<columns.length;j++){
            model.addAttribute(columns[j],values[j]);
        }
    }
}

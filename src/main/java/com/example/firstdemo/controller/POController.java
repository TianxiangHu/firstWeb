package com.example.firstdemo.controller;

/*
还没写好，暂时放这
待确认页面相关的控制逻辑
*/

import com.example.firstdemo.order.Order;
import com.example.firstdemo.product.Commodity;
import com.example.firstdemo.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;

import static com.example.firstdemo.mysql.JDBC_Delete.DELETEEQUAL;
import static com.example.firstdemo.mysql.JDBC_Select.GETAND;
import static com.example.firstdemo.mysql.JDBC_Select.GETEQUAL;

@Controller
public class POController {
    Order order;
    Commodity commodity;
    User user;

    int user_id = -1;
    final String visible = "visible";
    final String confirm="confirm";
    final int column_number = 6;
    final int commodity_number = 5;
    int current_commodities_number = 0;//记录当前显示的第一个商品在re中的序号
    List<String> re;//储存在MySQL中查询得到的多个order_id值

    final String[] columns_conditions = {
            "status",
            "buy_user_id"
    };
    final String[] columns = {
            "name",
            "image_adr",
            "seller",
            "number",
            "price",
            "introduction"
    };

    POController()
            throws SQLException,ClassNotFoundException{
        order=new Order();
        commodity=new Commodity();
        user=new User();
    }

    @RequestMapping("/anonymous/user_info/pending_order")
    public String pending_order(HttpSession session,Model model){
        if(session.getAttribute("user id")==null)
            return "redirect:/hello";
        user_id = (int) session.getAttribute("user id");
        //设置表格为不可见
        for (int i = 0; i < column_number; i++)
            init_one_visible(i, false, model);
        init_one_visible(6, false, model);
        init_one_visible(7, false, model);

        String[] values_condition = {"1", String.valueOf(user_id)};
        re = order.order_show.find_column("order_id", GETAND, columns_conditions, values_condition);
        init_one_visible(5, true, model);
        if (re != null) {
            current_commodities_number=0;
            show_one_page(model,current_commodities_number);
        }

        return "anonymous/user_info/pending_order";
    }

    @RequestMapping("GoPending_Order")
    public String GoPending_Order(){
        return "redirect:/anonymous/user_info/pending_order";
    }

    @RequestMapping("/anonymous/user_info/po_TurnDown")
    public String turnDown(Model model){
        if(re.size()-current_commodities_number>5){
            current_commodities_number+=5;
            show_one_page(model,current_commodities_number);
        }
        return "/anonymous/user_info/pending_order";
    }

    @RequestMapping("/anonymous/user_info/po_TurnUp")
    public String turnUp(Model model) {
        if (current_commodities_number >= 5) {
            current_commodities_number -= 5;
            show_one_page(model,current_commodities_number);
        }
        return "/anonymous/user_info/pending_order";
    }

    @RequestMapping("/anonymous/user_info/final_confirm")
    public String final_confirm(HttpServletRequest httpServletRequest, Model model){
        for (int i=0;i<commodity_number;i++){
            if(httpServletRequest.getParameter(confirm+i)!=null){
                int confirm_id=Integer.valueOf(httpServletRequest.getParameter(confirm+i));
                if(confirm_id>=0&&confirm_id<=4){
                    String confirm_order_id=re.get(current_commodities_number+confirm_id);
                    System.out.println("confirm order id："+confirm_order_id);
                    order.order_show.init_specific_table(GETEQUAL,"order_id",confirm_order_id);
                    String order_column = "status", order_condition = "order_id";
                    String[] values = {"2", String.valueOf(order.getOrder_id())};
                    order.order_change.change_one(order_column,order_condition,values);
                }
                else
                {
                    System.out.println(confirm_id);
                }
            }
        }
        String[] values_condition = {"1", String.valueOf(user_id)};
        re = order.order_show.find_column("order_id", GETAND, columns_conditions, values_condition);

        System.out.print("confirm!  ");
        System.out.println(re);
        init_one_visible(5, true, model);
        current_commodities_number=0;
        show_one_page(model,current_commodities_number);

        return "/anonymous/user_info/pending_order";
    }

    //设置搜索表格是否可见
    private void init_one_visible(int i, boolean flag, Model model) {
        String current_visible = visible + i;
        if (flag)
            model.addAttribute(current_visible, false);
        else
            model.addAttribute(current_visible, true);
    }

    //设置一行商品信息
    private void init_one_row(int i, Model model) {
        order.order_show.init_specific_table(GETEQUAL,"order_id",re.get(current_commodities_number+i));
        commodity.commodity_show.init_specific_table(GETEQUAL,"commodity_id",String.valueOf(order.getCommodity_id()));
        user.obtain_user_info.init_specific_table(GETEQUAL, "user_info", "user_id", String.valueOf(commodity.getUser_id()));

        String[] values = new String[columns.length];
        values[0] = commodity.getCommodity_name();
        values[1] = "../"+commodity.getPic_img_adr();
        values[2] = user.getName();
        values[3] = String.valueOf(order.getExist_number());
        values[4] = String.valueOf(order.getPrice());
        values[5] = commodity.getIntroduction();

        String temp;
        for (int j = 0; j < columns.length; j++) {
            temp = columns[j] + i;
            model.addAttribute(temp, values[j]);
        }
    }

    //显示一页的商品信息，从re的第start个元素开始显示
    private void show_one_page(Model model, int start) {
        //为了消除之前的影响，首先将除第一行外的所有行均设为不可见
        for (int i = 0; i < commodity_number; i++) {
            init_one_visible(i, false, model);
        }
        int current_page_number = re.size() - start;
        current_page_number = current_page_number > 5 ? 5 : current_page_number;
        for (int i=0;i<current_page_number;i++){
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
}

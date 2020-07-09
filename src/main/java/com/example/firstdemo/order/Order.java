package com.example.firstdemo.order;

/*
* 用来封装和订单有关的操作
* 实际上和对商品的操作基本上相同的，只是为了方便而多谢一个类
* */

import java.sql.SQLException;

public class Order {
    private int order_id,commodity_id,status,exist_number,buy_user_id;
    private String create_time;
    private float price;
    private final String table_name="order_info";

    public Order_Change order_change;
    public Order_Show order_show;
    public Order_AD order_ad;

    public Order()
    throws SQLException,ClassNotFoundException {
        order_change=new Order_Change(this);
        order_show=new Order_Show(this);
        order_ad=new Order_AD(this);
    }

    public boolean getOrder(String[] strs){
        if(strs.length==7){
            setOrder_id(Integer.valueOf(strs[0]));
            setCommodity_id(Integer.valueOf(strs[1]));
            setBuy_user_id(Integer.valueOf(strs[2]));
            setStatus(Integer.valueOf(strs[3]));
            setCreate_time(strs[4]);
            setPrice(Float.valueOf(strs[5]));
            setExist_number(Integer.valueOf(strs[6]));
            return true;
        }
        return false;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getCommodity_id() {
        return commodity_id;
    }

    public void setCommodity_id(int commodity_id) {
        this.commodity_id = commodity_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getExist_number() {
        return exist_number;
    }

    public void setExist_number(int exist_number) {
        this.exist_number = exist_number;
    }

    public int getBuy_user_id() {
        return buy_user_id;
    }

    public void setBuy_user_id(int buy_user_id) {
        this.buy_user_id = buy_user_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getTable_name() {
        return table_name;
    }
}

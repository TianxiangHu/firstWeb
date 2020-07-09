package com.example.firstdemo.product;

/*
* 用于封装和商品有关的操作，主要为显示部分、增加商品、更新商品、修改商品信息
* 此外，用户的收货地址也由该包管理
* 则管理的表为"commodity"
* */

import java.sql.SQLException;

public class Commodity {
    private int commodity_id,user_id,status,exist_number;
    private String commodity_name,introduction,pic_img_adr,remarks;
    private float price;
    private final String table_name="commodity";


    public Commodity_Show commodity_show;
    public Commodity_AD commodity_ad;
    public Commodity_Change commodity_change;

    public Commodity()
        throws SQLException,ClassNotFoundException{
        commodity_show=new Commodity_Show(this);
        commodity_ad=new Commodity_AD(this);
        commodity_change=new Commodity_Change(this);
    }

    //填写Commodity表一行中的相关变量
    public boolean getCommodity(String[] strs){
        if(strs.length==9) {
            setCommodity_id(Integer.valueOf(strs[0]));
            setUser_id(Integer.valueOf(strs[1]));
            setCommodity_name(strs[2]);
            setPrice(Float.valueOf(strs[3]));
            setIntroduction(strs[4]);
            setExist_number(Integer.valueOf(strs[5]));
            setPic_img_adr(strs[6]);
            setStatus(Integer.valueOf(strs[7]));
            setRemarks(strs[8]);
            return true;
        }
        return false;
    }

    public int getCommodity_id() {
        return commodity_id;
    }

    public void setCommodity_id(int commodity_id) {
        this.commodity_id = commodity_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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

    public String getCommodity_name() {
        return commodity_name;
    }

    public void setCommodity_name(String commodity_name) {
        this.commodity_name = commodity_name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getPic_img_adr() {
        return pic_img_adr;
    }

    public void setPic_img_adr(String pic_img_adr) {
        this.pic_img_adr = pic_img_adr;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getTable_name() {
        return table_name;
    }
}

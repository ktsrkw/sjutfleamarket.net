package com.wt.pojo;

import java.util.Date;

public class Goods {
    private int goodsid;
    private int userid;
    private Date deliveryTime;
    private String title;
    private String description;
    //    分类：
//    学习用品
//    交通工具
//    生活日用品
//    衣物
//    电子产品
//    书籍
//    其他
    private String category;
    private float originalPrice;
    private float price;
    //状态：0为下架，1为未下架
    private byte status;

    public Goods() {
    }

    public Goods(int goodsid, int userid, Date deliveryTime, String title, String description, String category, float originalPrice, float price, byte status) {
        this.goodsid = goodsid;
        this.userid = userid;
        this.deliveryTime = deliveryTime;
        this.title = title;
        this.description = description;
        this.category = category;
        this.originalPrice = originalPrice;
        this.price = price;
        this.status = status;
    }

    public int getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(int goodsid) {
        this.goodsid = goodsid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public float getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(float originalPrice) {
        this.originalPrice = originalPrice;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "goods{" +
                "goodsid=" + goodsid +
                ", userid=" + userid +
                ", deliveryTime=" + deliveryTime +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", originalPrice=" + originalPrice +
                ", price=" + price +
                ", status=" + status +
                '}';
    }
}

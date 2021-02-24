package com.wt.pojo;

public class Images {
    private String imgurl;
    private int goodsid;

    public Images() {
    }

    public Images(String imgurl, int goodsid) {
        this.imgurl = imgurl;
        this.goodsid = goodsid;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public int getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(int goodsid) {
        this.goodsid = goodsid;
    }

    @Override
    public String toString() {
        return "images{" +
                "imgurl='" + imgurl + '\'' +
                ", goodsid=" + goodsid +
                '}';
    }
}

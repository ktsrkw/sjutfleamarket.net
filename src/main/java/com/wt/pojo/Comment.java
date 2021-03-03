package com.wt.pojo;

import java.util.Date;

public class Comment {
    private int commentid;
    private int userid;
    private int goodsid;
    private Date commentDeliveryTime;
    private String content;

    public Comment() {
    }

    public Comment(int commentid, int userid, int goodsid, Date commentDeliveryTime, String content) {
        this.commentid = commentid;
        this.userid = userid;
        this.goodsid = goodsid;
        this.commentDeliveryTime = commentDeliveryTime;
        this.content = content;
    }

    public int getCommentid() {
        return commentid;
    }

    public void setCommentid(int commentid) {
        this.commentid = commentid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(int goodsid) {
        this.goodsid = goodsid;
    }

    public Date getCommentDeliveryTime() {
        return commentDeliveryTime;
    }

    public void setCommentDeliveryTime(Date commentDeliveryTime) {
        this.commentDeliveryTime = commentDeliveryTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentid=" + commentid +
                ", userid=" + userid +
                ", goodsid=" + goodsid +
                ", commentDeliveryTime=" + commentDeliveryTime +
                ", content='" + content + '\'' +
                '}';
    }
}

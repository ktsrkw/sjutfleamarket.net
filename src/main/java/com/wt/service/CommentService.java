package com.wt.service;

import com.wt.pojo.Comment;

import java.util.List;

public interface CommentService {
    //根据商品id拿到一些评论
    List<Comment> getCommentsByGoodsId(int goodsid);
}

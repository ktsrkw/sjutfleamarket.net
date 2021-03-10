package com.wt.mapper;

import com.wt.pojo.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CommentMapper {
    //根据商品id拿到一些评论
    List<Comment> getCommentsByGoodsId(int goodsid);

    //向数据库中添加一条评论
    int addAComment(Comment comment);

    //根据goodsid删除评论
    int deleteCommentsByGoodsId(int goodsid);

    //根据userid删除评论
    int deleteCommentsByUserId(int userid);

}

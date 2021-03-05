package com.wt.service;

import com.wt.mapper.CommentMapper;
import com.wt.pojo.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService{
    @Autowired
    CommentMapper commentMapper;

    @Override
    public List<Comment> getCommentsByGoodsId(int goodsid) {
        return commentMapper.getCommentsByGoodsId(goodsid);
    }
}
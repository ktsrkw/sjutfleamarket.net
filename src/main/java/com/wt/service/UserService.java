package com.wt.service;

import com.wt.pojo.User;

import java.util.List;

public interface UserService {
    //得到所有用户数据
    List<User> getAllUsers();

    //根据商品id得到发布者信息
    User getUserByGoodsid(int goodsid);
}

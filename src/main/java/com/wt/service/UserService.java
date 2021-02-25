package com.wt.service;

import com.wt.pojo.User;

import java.util.List;

public interface UserService {
    //得到所有用户数据
    List<User> getAllUsers();

    //根据商品id得到发布者信息
    User getUserByGoodsid(int goodsid);

    //根据用户名或用户邮箱得到用户信息
    User getUserByUsername(String username);

    //插入一个用户的数据(有生日数据)
    int insertAnUserWithBirthday(User user);

    //插入一个用户的数据(没有生日数据)
    int insertAnUserWithoutBirthday(User user);
}

package com.wt.service;

import com.wt.mapper.UserMapper;
import com.wt.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public List<User> getAllUsers() {
        return userMapper.getAllUsers();
    }

    @Override
    public User getUserByGoodsid(int goodsid) {
        return userMapper.getUserByGoodsid(goodsid);
    }
}

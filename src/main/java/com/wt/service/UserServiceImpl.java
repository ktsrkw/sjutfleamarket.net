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
    public User getUserById(int userid) {
        return userMapper.getUserById(userid);
    }

    @Override
    public User getUserByGoodsid(int goodsid) {
        return userMapper.getUserByGoodsid(goodsid);
    }

    @Override
    public User getUserByUsername(String username) {
        return userMapper.getUserByUsername(username);
    }

    @Override
    public int insertAnUserWithBirthday(User user) {
        return userMapper.insertAnUserWithBirthday(user);
    }

    @Override
    public int insertAnUserWithoutBirthday(User user) {
        return userMapper.insertAnUserWithoutBirthday(user);
    }

    @Override
    public int updateUserWithBirthday(User user) {
        return userMapper.updateUserWithBirthday(user);
    }

    @Override
    public int updateUserWithoutBirthday(User user) {
        return userMapper.updateUserWithoutBirthday(user);
    }
}

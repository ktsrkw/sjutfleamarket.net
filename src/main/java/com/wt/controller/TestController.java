package com.wt.controller;

import com.wt.mapper.UserMapper;
import com.wt.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class TestController {
    @Autowired
    UserMapper userMapper;

    @RequestMapping("/test")
    @ResponseBody
    public String test() {
        List<User> users = userMapper.getAllUsers();
        return users.toString();
    }
}

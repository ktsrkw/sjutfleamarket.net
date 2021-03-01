package com.wt.mapper;

import com.wt.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper {
    //得到所有用户数据
    List<User> getAllUsers();

    //通过id得到用户
    User getUserById(int userid);

    //根据商品id得到发布者信息
    User getUserByGoodsid(int goodsid);

    //根据用户名或用户邮箱得到用户信息
    User getUserByUsername(String username);

    //插入一个用户的数据(有生日数据)
    int insertAnUserWithBirthday(User user);

    //插入一个用户的数据(没有生日数据)
    int insertAnUserWithoutBirthday(User user);

    //更新用户的信息(有生日数据)
    int updateUserWithBirthday(User user);

    //更新用户的信息(没有生日数据)
    int updateUserWithoutBirthday(User user);

    //根据id删除用户表的记录
    int deleteUserById(int userid);
}

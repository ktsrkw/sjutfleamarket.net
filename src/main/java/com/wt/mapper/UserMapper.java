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

    //根据商品id得到发布者信息
    User getUserByGoodsid(int goodsid);
}

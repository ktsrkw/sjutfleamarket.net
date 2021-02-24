package com.wt.mapper;

import com.wt.pojo.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface GoodsMapper {
    //得到所有商品的数据
    List<Goods> getAllGoods();

    //根据id得到商品信息
    Goods getGoodsById(int goodsid);

    //根据分类的类型得到商品
    List<Goods> getGoodsByCategory(String category);

    //根据输入模糊查询名称得到商品
    List<Goods> getGoodsByInputName(String searchContent);
}

package com.wt.service;

import com.wt.pojo.Goods;

import java.util.List;

public interface GoodsService {
    //得到所有商品的数据
    List<Goods> getAllGoods();

    //根据id得到商品信息
    Goods getGoodsById(int goodsid);

    //根据分类的类型得到商品
    List<Goods> getGoodsByCategory(String category);

    //根据输入模糊查询名称得到商品
    List<Goods> getGoodsByInputName(String searchContent);

    //增加一条商品记录
    int addAGoods(Goods goods);

    //根据商品名得到商品的信息
    Goods getGoodsByTitle(String title);

    //根据userid得到商品
    List<Goods> getGoodsByUserid(int userid);

    //修改商品的信息
    int updateGoods(Goods goods);

}

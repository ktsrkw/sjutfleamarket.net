package com.wt.mapper;

import com.wt.pojo.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
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

    //增加一条商品记录
    int addAGoods(Goods goods);

    //根据商品名得到商品的信息
    Goods getGoodsByTitle(String title);

    //根据userid得到商品
    List<Goods> getGoodsByUserid(int userid);

    //修改商品的信息
    int updateGoods(Goods goods);

    //根据goodsid下架商品
    int offShelfGoodsByGoodsid(int goodsid);

    //根据goodsid下架商品
    int onShelfGoodsByGoodsid(int goodsid);

    //根据goodsid删除商品
    int deleteGoodsByGoodsId(int goodsid);

    //根据goodsid获得该goods的userid
    int getUserIdByGoodsId(int goodsid);

    //根据日期与名称得到商品id
    int getGoodsIdByDateAndTitle(@Param("date") String date,@Param("title") String title);

}

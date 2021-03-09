package com.wt.service;

import com.wt.mapper.GoodsMapper;
import com.wt.pojo.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    GoodsMapper goodsMapper;

    @Override
    public List<Goods> getAllGoods() {
        return goodsMapper.getAllGoods();
    }

    @Override
    public Goods getGoodsById(int goodsid) {
        return goodsMapper.getGoodsById(goodsid);
    }

    @Override
    public List<Goods> getGoodsByCategory(String category) {
        return goodsMapper.getGoodsByCategory(category);
    }

    @Override
    public List<Goods> getGoodsByInputName(String searchContent) {
        return goodsMapper.getGoodsByInputName(searchContent);
    }

    @Override
    public int addAGoods(Goods goods) {
        return goodsMapper.addAGoods(goods);
    }

    @Override
    public Goods getGoodsByTitle(String title) {
        return goodsMapper.getGoodsByTitle(title);
    }

    @Override
    public List<Goods> getGoodsByUserid(int userid) {
        return goodsMapper.getGoodsByUserid(userid);
    }

    @Override
    public int updateGoods(Goods goods) {
        return goodsMapper.updateGoods(goods);
    }
}

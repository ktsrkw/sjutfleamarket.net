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
}

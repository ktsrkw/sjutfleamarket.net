package com.wt.service;

import com.wt.mapper.ImagesMapper;
import com.wt.pojo.Images;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ImagesServiceImpl implements ImagesService{
    @Autowired
    ImagesMapper imagesMapper;

    @Override
    public int addAnImage(Images images) {
        return imagesMapper.addAnImage(images);
    }

    @Override
    public List<Images> getImagesByGoodsId(int goodsid) {
        return imagesMapper.getImagesByGoodsId(goodsid);
    }

    @Override
    public int deleteImagesByGoodsid(int goodsid) {
        return imagesMapper.deleteImagesByGoodsid(goodsid);
    }
}

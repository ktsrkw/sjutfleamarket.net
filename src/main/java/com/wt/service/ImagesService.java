package com.wt.service;

import com.wt.pojo.Images;

import java.util.List;

public interface ImagesService {
    //向表中添加一条记录
    int addAnImage(Images images);

    //根据goodsid得到图片的数据
    List<Images> getImagesByGoodsId(int goodsid);
}

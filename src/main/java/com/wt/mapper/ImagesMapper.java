package com.wt.mapper;

import com.wt.pojo.Images;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ImagesMapper {
    //向表中添加一条记录
    int addAnImage(Images images);

    //根据goodsid得到图片的数据
    List<Images> getImagesByGoodsId(int goodsid);

    //根据goodsid删除图片
    int deleteImagesByGoodsid(int goodsid);
}

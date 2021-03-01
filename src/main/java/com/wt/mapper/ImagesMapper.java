package com.wt.mapper;

import com.wt.pojo.Images;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ImagesMapper {
    //向表中添加一条记录
    int addAnImage(Images images);
}

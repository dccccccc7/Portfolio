package com.example.school.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.school.entity.baseEntity.currentchoose;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface currentchooseMapper extends BaseMapper<currentchoose> {
    @Update("update currentchoose " +
            "set present=0 " +
            "where cid=#{cid}")
    Integer refreshPresent(@Param("cid") String cid);
}

package com.example.school.mapper;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.school.entity.baseEntity.course;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@CacheNamespace
public interface TestSessionMapper {
    @Select("select * " +
            "from course")
    List<course> testPage(Page page);

    @Select("select cid,cname,tid " +
            "from course")
    List<course> testList();

    @Select("select cid,cname,tid " +
            "from course")
    List<course> testList2();

    @Update("update course " +
            "set cname = '666666' " +
            "where cid = '666'")
    int updateTestData();
}

package com.example.school.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.school.entity.baseEntity.course;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

@CacheNamespace
public interface TestMapper {
    @Select("select * " +
            "from course")
    List<course> testPage(Page page);

    @Select("select * " +
            "from course")
    List<course> testList();

    @Select("select * " +
            "from course " +
            "where tid = 1")
    List<course> testList2();

    @Update("update course " +
            "set cname = '666666' " +
            "where cid = '666'")
    int updateTestData();
}

package com.example.school.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.school.entity.baseEntity.course;
import com.example.school.entity.baseEntity.currentsemester;
import com.example.school.entity.baseEntity.historycourse;
import com.example.school.entity.complexEntity.viewCourse;
import com.example.school.entity.complexEntity.viewTableBlock;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface courseMapper extends BaseMapper<course> {
    @Select("select distinct cid,cname,tid,tname,num,total,total-num as `left`,`describes`,credit " +
            "from course natural join teacher " +
            "${ew.customSqlSegment} ")
    Page<viewCourse> getAllViewCourses(Page page,@Param(Constants.WRAPPER) Wrapper wrapper);
    @Select("select cid,cname,tid,tname,weekday,dayorder,place,beginWeek,endWeek,credit " +
            "from prechoose natural join arrange " +
            "natural join course natural join teacher " +
            "where sid=#{sid}")
    List<viewTableBlock> getPreTable(@Param("sid")Integer sid);
    @Select("select cid,cname,tid,tname,weekday,dayorder,place,beginWeek,endWeek,score,credit " +
            "from currentchoose natural join arrange natural join course natural join teacher " +
            "where sid=#{sid}")
    List<viewTableBlock> getCurTable(@Param("sid")Integer sid);
    @Select("select cid,cname,weekday,dayorder,place,beginWeek,endWeek " +
            "from course natural join arrange " +
            "where cid=#{cid}")
    List<viewTableBlock> getOneCourseTable(@Param("cid")String cid);
    @Select("select distinct choosed.cid " +
            "from (select cid,weekday,dayorder " +
            "from arrange natural join ${checkTable} " +
            "where sid=#{sid}) as choosed join " +
            "(select cid,weekday,dayorder " +
            "from arrange " +
            "where cid=#{cid}) as choosing using(weekday,dayorder)")
    List<Integer> selectionTimeCheck(@Param("checkTable")String checkTable,@Param("sid") Integer sid, @Param("cid") String cid);
    @Select("select distinct cid,cname,tid,tname,num,total,total-num as `left`,`describes`,credit " +
            "from ${tableName} natural join course natural join teacher " +
            "${ew.customSqlSegment} ")
    List<viewCourse> getMyViewCourse(@Param("tableName")String tableName,@Param(Constants.WRAPPER) Wrapper wrapper);
    @Select("select distinct cid,cname,tid,tname,credit,score " +
            "from currentchoose natural join course natural join teacher " +
            "${ew.customSqlSegment} ")
    List<viewCourse> getMyTrueCourse(@Param(Constants.WRAPPER) Wrapper wrapper);
    @Select("select cid,cname,tid,tname,`year`,semester,score,`describes`,credit " +
            "from historychoose natural join historycourse " +
            "${ew.customSqlSegment} ")
    List<historycourse> getHistoryCourse(@Param(Constants.WRAPPER) Wrapper wrapper);
    @Select("select cid,cname,num " +
            "from course natural join teacher " +
            "${ew.customSqlSegment} ")
    Page<viewCourse> getTeacherCourseMini(Page page,@Param(Constants.WRAPPER)Wrapper wrapper);
    @Select("select cid,cname,num,credit,`describes` " +
            "from course natural join teacher " +
            "${ew.customSqlSegment} ")
    Page<viewCourse> getTeacherCourseFull(Page page,@Param(Constants.WRAPPER) Wrapper wrapper);
    @Select("select sid,sname,score " +
            "from currentchoose natural join student " +
            "where cid=#{cid}")
    List<viewCourse> getCourseStudents(@Param("cid")String cid);
    @Select("select * " +
            "from currentsemester")
    List<currentsemester> getSEMESTER();
    @Insert("insert into currentchoose(sid,cid) " +
            "select * " +
            "from prechoose")
    int preTocurCourse();
    @Update("update course " +
            "set cname=#{cname},tid=#{tid},total=#{total} " +
            "${ew.customSqlSegment}")
    int updateCourse(@Param("cname")String cname,@Param("tid")Integer tid,@Param("total")Integer total,@Param(Constants.WRAPPER) Wrapper wrapper);
    @Update("update currentsemester " +
            "set `year`=#{year},semester=#{semester}")
    int setSEMESTER(@Param("year")String year,@Param("semester")Integer semester);
    @Update("update currentchoose " +
            "set score=#{score} " +
            "where cid=#{cid} and sid=#{sid}")
    int setScore(@Param("cid")String cid,@Param("sid")Integer sid,@Param("score")Integer score);
    @Update("update course " +
            "set num=num+1 " +
            "where cid=#{cid}")
    void courseNumPlus(@Param("cid") String cid);
    @Update("update course " +
            "set num=num-1 " +
            "where cid=#{cid}")
    void courseNumReduce(@Param("cid") String cid);
    @Insert("insert into historychoose " +
            "select sid,sname,cid,#{year},#{semester},score " +
            "from currentchoose natural join student")
    void curChooseToHisChoose(@Param("year")String year,@Param("semester")Integer semester);

    @Insert("insert into historycourse " +
            "select cid,cname,tid,tname,#{year},#{semester},describes,credit " +
            "from course natural join teacher")
    void curCourseToHisCourse(@Param("year")String year,@Param("semester")Integer semester);

    @Select("select count(*)+1 as rank " +
            "from course natural join currentchoose natural join (" +
                "select sid " +
                "from student " +
                "${ew.customSqlSegment} " +
            ") as stuRange " +
            "where cid=#{cid} and score is not null and score > (" +
                "select score " +
                "from currentchoose " +
                "where sid=#{sid} and cid=#{cid}" +
            ")")
    Integer getRank(@Param(Constants.WRAPPER) Wrapper wrapper,@Param("cid") String cid,@Param("sid") Integer sid);

    @Select("select count(*) as total " +
            "from student natural join currentchoose " +
            "${ew.customSqlSegment}")
    Integer getSpecificTotal(@Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("select cid,sid,sname,`present` " +
            "from currentchoose natural join student " +
            "where cid=#{cid}")
    List<viewCourse> getPresent(@Param("cid") String cid);
}

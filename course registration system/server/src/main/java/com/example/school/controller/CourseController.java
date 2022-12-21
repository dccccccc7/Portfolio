package com.example.school.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.school.common.Result;
import com.example.school.entity.baseEntity.*;
import com.example.school.entity.complexEntity.setScoreInfo;
import com.example.school.entity.complexEntity.viewCourse;
import com.example.school.entity.complexEntity.viewTableBlock;
import com.example.school.entity.complexEntity.viewUser;
import com.example.school.mapper.*;
import io.swagger.annotations.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.swing.text.View;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


//@ApiOperation(value = "",
//        notes = "")
//@ApiImplicitParams({
//        @ApiImplicitParam(name = "",value = "",dataTypeClass = ,required = true)
//})
//@ApiResponses({
//        @ApiResponse(code = 200,message = "成功返回对象。<br>" +
//                "code=0:")
//})


@Api(tags = "课程信息操作")
@RestController
@RequestMapping("/course")
@Transactional
public class CourseController {
    @Resource
    courseMapper courseMapper;
    @Resource
    arrangeMapper arrangeMapper;
    @Resource
    prechooseMapper prechooseMapper;
    @Resource
    flagsMapper flagsMapper;
    @Resource
    currentchooseMapper currentchooseMapper;

    @GetMapping("/getAllCourse")
    @ApiOperation(value = "查找全课程",
            notes = "按照指定的课程名、星期、节次信息从全部课程中进行搜索，用于向学生呈现可选课程")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "courseName",value = "指定的课程名模糊查询关键字",dataTypeClass = String.class,defaultValue = "null"),
            @ApiImplicitParam(name = "weekday",value = "指定的星期列表(空时为不指定)",allowMultiple = true,dataTypeClass = Integer.class,defaultValue = "null"),
            @ApiImplicitParam(name = "order",value = "指定的节次列表(空时为不指定)",allowMultiple = true,dataTypeClass = Integer.class,defaultValue = "null"),
            @ApiImplicitParam(name = "pageNum",value = "页码，用于分页",dataTypeClass = Integer.class,required = true,defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize",value = "页大小，用于分页",dataTypeClass = Integer.class,required = true,defaultValue = "10")
    })
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功返回对象。<br>" +
                    "code=0:data中返回一个Page对象，记录内容的属性为records，内容为一个List&lt;viewCourse&gt;，记录总数的属性为total")
    })
    public Result<?> getAllViewCourse(@RequestParam(defaultValue = "")String courseName,
                                      @RequestParam(defaultValue = "")List<Integer> weekday,
                                      @RequestParam(defaultValue = "")List<Integer> order,
                                      @RequestParam(defaultValue = "1")Integer pageNum,
                                      @RequestParam(defaultValue = "10")Integer pageSize){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.like("cname",courseName);
        if(weekday.size()!=0)
            wrapper.in("weekday",weekday);
        if(order.size()!=0)
            wrapper.in("dayorder",order);
        Page<viewCourse> answer = courseMapper.getAllViewCourses(new Page(pageNum,pageSize),wrapper);
        List<arrange> empt = new ArrayList<>();
        List<arrange> setting;
        for(viewCourse i:answer.getRecords()){
            setting = arrangeMapper.selectList(Wrappers.<arrange>lambdaQuery()
                    .eq(arrange::getCid,i.getCid()));
            if(setting==null){
                i.setArranges(empt);
            }
            else{
                i.setArranges(setting);
            }
        }
        return Result.success(answer);
    }

    @PostMapping("/selection")
    @ApiOperation(value = "插入课程选择",
            notes = "给出一个选课信息，根据这个选课信息完成选课操作（包括录入选课信息和减少课余量）")
    @ApiImplicitParam(name = "selecting",value = "一个选课信息对象",dataTypeClass = prechoose.class,required = true)
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功返回对象。<br>" +
                    "code=0:返回一个无数据的的Result对象，用于表示操作成功<br>" +
                    "code=-1:与已选课程时间冲突，msg中会指明冲突的课序号<br>" +
                    "code=-2:与正式上课的课程时间冲突，msg中会指明冲突的课序号<br>" +
                    "code=-7:未知错误")
    })
    public Result<?> selectionPerform(@RequestBody prechoose selecting){
        List<Integer> preCheck = courseMapper.selectionTimeCheck("prechoose",selecting.getSid(),selecting.getCid());
        List<Integer> curCheck = courseMapper.selectionTimeCheck("currentchoose",selecting.getSid(),selecting.getCid());
        if(preCheck.size()==0 && curCheck.size()==0){
            prechooseMapper.insert(selecting);
            courseMapper.courseNumPlus(selecting.getCid());
            return Result.success();
        }
        else{
            if(preCheck.size()!=0){
                return Result.error("-1","与已选课时间冲突：课序号"+preCheck.get(0));
            }
            if(curCheck.size()!=0){
                return Result.error("-2","与正式确定的课时间冲突：课序号"+curCheck.get(0));
            }
            return Result.error("-7","未知错误");
        }
    }

    @PostMapping("/drop/{choosed}")
    @ApiOperation(value = "退课操作",
            notes = "给出一条选课信息和一个代表该课程是预选还是正式课程的布尔量，根据这两个信息删除指定的选课信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dropping",value = "一个选课信息对象，用于指明我们要删除哪个课程",dataTypeClass = prechoose.class,required = true),
            @ApiImplicitParam(name = "choosed",value = "一个布尔量，为true时代表这个课已经正式确定，正在上课；为false时代表这个课还属于预选阶段",dataTypeClass = Boolean.class,required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功返回对象。<br>" +
                    "code=0:返回无数据Result<br>" +
                    "code=-1:检测到删除了0条数据，删除行为是不正常的")
    })
    public Result<?> dropPerform(@RequestBody prechoose dropping,@PathVariable Boolean choosed){
        int rows;
        if(choosed){
            rows = currentchooseMapper.delete(Wrappers.<currentchoose>lambdaQuery()
                    .eq(currentchoose::getCid,dropping.getCid())
                    .eq(currentchoose::getSid,dropping.getSid()));
        }
        else{
            rows = prechooseMapper.delete(Wrappers.<prechoose>lambdaQuery()
                    .eq(prechoose::getCid,dropping.getCid())
                    .eq(prechoose::getSid,dropping.getSid()));
        }
        if(rows!=0){
            courseMapper.courseNumReduce(dropping.getCid());
            return Result.success();
        }
        return Result.error("-1","未进行删除");
    }

    @GetMapping("/getMyCourse")
    @ApiOperation(value = "获取个人所有课程",
            notes = "根据输入的学生id，获取一个学生的所有预选课和正式课程")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sid",value = "所要查询的课程所属学生的id",dataTypeClass = Integer.class,required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功返回对象。<br>" +
                    "code=0:data中返回一个List&lt;viewCourse&gt;，viewCourse可参考models<br>" +
                    "code=-1:输入的sid出现错误")
    })
    public Result<?> getMyViewCourse(@RequestParam(defaultValue = "-1")Integer sid){
        QueryWrapper wrapper = new QueryWrapper();
        if(sid!=-1){
            wrapper.eq("sid",sid);
            List<viewCourse> answer = courseMapper.getMyViewCourse("prechoose",wrapper);
            answer.addAll(courseMapper.getMyTrueCourse(wrapper));
            List<arrange> empt = new ArrayList<>();
            List<arrange> setting;
            for(viewCourse i:answer){
                setting = arrangeMapper.selectList((Wrappers.<arrange>lambdaQuery()
                        .eq(arrange::getCid,i.getCid())));
                if(setting==null){
                    i.setArranges(empt);
                }
                else{
                    i.setArranges(setting);
                }
            }
            return Result.success(answer);
        }
        else{
            return Result.error("-1","id信息错误");
        }
    }


    @GetMapping("/getMyTrueCourse")
    @ApiOperation(value = "获取个人正式课程",
            notes = "根据输入的学生id，获取一个学生的正式课程。处于选课阶段的课程不会被取得。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sid",value = "所要查询的课程所属学生的id",dataTypeClass = Integer.class,required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功返回对象。<br>" +
                    "code=0:data中返回一个List&lt;viewCourse&gt;，viewCourse可参考models<br>" +
                    "code=-1:输入的sid出现错误")
    })
    public Result<?> getMyTrueCourse(@RequestParam(defaultValue = "-1")Integer sid){
        QueryWrapper wrapper = new QueryWrapper();
        if(sid!=-1){
            wrapper.eq("sid",sid);
            List<viewCourse> answer = courseMapper.getMyTrueCourse(wrapper);
            List<arrange> empt = new ArrayList<>();
            List<arrange> setting;
            for(viewCourse i:answer){
                setting = arrangeMapper.selectList((Wrappers.<arrange>lambdaQuery()
                        .eq(arrange::getCid,i.getCid())));
                if(setting==null){
                    i.setArranges(empt);
                }
                else{
                    i.setArranges(setting);
                }
            }
            return Result.success(answer);
        }else{
            return Result.error("-1","id信息错误");
        }
    }

    @GetMapping("/getTable")
    @ApiOperation(value = "获取学生课表数据",
            notes = "根据输入的学生id，获取一个学生的课表信息。<br>" +
                    "这里的课程信息已经经过了组装，是一个ArrayList&lt;HashMap&lt;Integer,viewTableBlock&gt;&gt;的结构，便于进行二维的对应。" +
                    "ArrayList的顺序对应节次，HashMap中的对应关系是周X与课程信息的对应。<br>" +
                    "只有处于选课阶段时会把预选课程的内容也加入返回值")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sid",value = "所要查询的课表所属学生的id",dataTypeClass = Integer.class,required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功返回对象。<br>" +
                    "code=0:data中返回一个ArrayList&lt;HashMap&lt;Integer,viewTableBlock&gt;&gt;，具体意义参见接口介绍")
    })
    public Result<?> getTable(@RequestParam(defaultValue = "-1")Integer sid){

        ArrayList<HashMap<Integer, viewTableBlock>> tableData = new ArrayList<>(5);
        for(int use = 0;use < 5;use++){
            tableData.add(new HashMap<>());
        }
        flags CHOOSING = flagsMapper.selectById("CHOOSING");
        List<viewTableBlock> Table;
        if(CHOOSING.isFlag()){
            Table = courseMapper.getPreTable(sid);
            Table.addAll(courseMapper.getCurTable(sid));
        }else{
            Table = courseMapper.getCurTable(sid);
        }

        for(viewTableBlock i : Table){
            tableData.get(i.getDayorder()-1).put(i.getWeekday(),i);
        }
        return Result.success(tableData);
    }

    @GetMapping("/getCourseArrangeTable")
    @ApiOperation(value = "获取课程安排",
            notes = "根据输入的课程id，获取这个课程的课表化信息<br>" +
                    "利用ArrayList&lt;HashMap&lt;Integer,viewTableBlock&gt;&gt;的结构包装课程的安排<br>" +
                    "ArrayList的顺序对应节次，HashMap中的对应关系是周X与课程信息的对应。<br>" +
                    "这个包装方式和获取学生课表数据接口基本一样")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cid",value = "所要查询的课程安排对应id",dataTypeClass = String.class,required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功返回对象。<br>" +
                    "code=0:data中返回一个ArrayList&lt;HashMap&lt;Integer,viewTableBlock&gt;&gt;，具体意义参见接口介绍")
    })
    public Result<?> getCourseArrangeTable(@RequestParam(defaultValue = "")String cid){
        ArrayList<HashMap<Integer, viewTableBlock>> tableData = new ArrayList<>(5);
        for(int use = 0;use < 5;use++){
            tableData.add(new HashMap<>());
        }
        List<viewTableBlock> Table = courseMapper.getOneCourseTable(cid);
        for(viewTableBlock i : Table){
            tableData.get(i.getDayorder()-1).put(i.getWeekday(),i);
        }
        return Result.success(tableData);
    }

    @GetMapping("/getHistoryCourse")
    @ApiOperation(value = "获取历史课程",
            notes = "根据输入的学生id，获取一个学生的历史课程信息。<br>" +
                    "这些课程已经结课，存入历史记录了。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sid",value = "所要查询的历史课程所属学生的id",dataTypeClass = Integer.class,required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功返回对象。<br>" +
                    "code=0:data中返回一个List&lt;historycourse&gt;，historycourse参见models<br>" +
                    "code=-1:输入的sid出现错误")
    })
    public Result<?> getHistoryCourse(@RequestParam(defaultValue = "-1")Integer sid){
        QueryWrapper wrapper = new QueryWrapper();
        if(sid!=-1){
            wrapper.eq("sid",sid);
            List<historycourse> answer = courseMapper.getHistoryCourse(wrapper);
            return Result.success(answer);
        }else{
            return Result.error("-1","id信息错误");
        }
    }

    @GetMapping("/getTeacherCourseMini")
    @ApiOperation(value = "获取教授的课程",
            notes = "根据输入的教师id，获取一个老师所教授的所有课程的基础信息。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tid",value = "所要查询的课程所属教师的id",dataTypeClass = Integer.class,required = true),
            @ApiImplicitParam(name = "pageNum",value = "页码，用于分页",dataTypeClass = Integer.class,required = true,defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize",value = "页大小，用于分页",dataTypeClass = Integer.class,required = true,defaultValue = "10")
    })
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功返回对象。<br>" +
                    "code=0:data中返回一个Page对象，记录内容的属性为records，内容为一个List&lt;viewCourse&gt;，记录总数的属性为total<br>" +
                    "code=-1:教师id错误")
    })
    public Result<?> getTeacherCourseMini(@RequestParam(defaultValue = "-1")Integer tid,
                                          @RequestParam(defaultValue = "1")Integer pageNum,
                                          @RequestParam(defaultValue = "10")Integer pageSize){
        QueryWrapper wrapper = new QueryWrapper();
        if(tid!=-1){
            wrapper.eq("tid",tid);
            Page<viewCourse> answer = courseMapper.getTeacherCourseMini(new Page(pageNum,pageSize),wrapper);
            return Result.success(answer);
        }
        else{
            return Result.error("-1","错误的教师id");
        }
    }

    @GetMapping("/getTeacherCourseFull")
    @ApiOperation(value = "获取教授的课程——满配版",
            notes = "根据输入的教师id，获取一个老师所教授的所有课程的信息。相比getTeacherCourseMini信息更多")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tid",value = "所要查询的课程所属教师的id",dataTypeClass = Integer.class,required = true),
            @ApiImplicitParam(name = "pageNum",value = "页码，用于分页",dataTypeClass = Integer.class,required = true,defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize",value = "页大小，用于分页",dataTypeClass = Integer.class,required = true,defaultValue = "10")
    })
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功返回对象。<br>" +
                    "code=0:data中返回一个Page对象，记录内容的属性为records，内容为一个List&lt;viewCourse&gt;，记录总数的属性为total<br>" +
                    "code=-1:教师id错误")
    })
    public Result<?> getTeacherCourseFull(@RequestParam(defaultValue = "-1") Integer tid,
                                          @RequestParam(defaultValue = "1")Integer pageNum,
                                          @RequestParam(defaultValue = "10")Integer pageSize){
        QueryWrapper wrapper = new QueryWrapper();
        if(tid!=-1){
            wrapper.eq("tid",tid);
            Page<viewCourse> answer = courseMapper.getTeacherCourseFull(new Page(pageNum,pageSize),wrapper);
            for(viewCourse i:answer.getRecords()){
                i.setArranges(arrangeMapper.selectList(Wrappers.<arrange>lambdaQuery()
                        .eq(arrange::getCid,i.getCid())));
            }
            return Result.success(answer);
        }
        else{
            return Result.error("-1","错误的教师id");
        }
    };

    @GetMapping("/getCourseStudents")
    @ApiOperation(value = "获取指定课的所有学生",
            notes = "根据给定的课序号，获取这个课的所有学生的选课信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cid",value = "所要查询的课序号",dataTypeClass = String.class,required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功返回对象。<br>" +
                    "code=0:data中返回一个List&lt;viewCourse&gt:，这个viewCourse对象中只注入了sid、sname、score<br>" +
                    "code=-1:课程id错误")
    })
    public Result<?> getCourseStudents(@RequestParam(defaultValue = "")String cid){
        if(cid.equals("")){
            return Result.error("-1","错误的课程id");
        }
        else{
            List<viewCourse> answer=courseMapper.getCourseStudents(cid);
            return Result.success(answer);
        }
    }

    @GetMapping("/getPresent")
    @ApiOperation(value = "获取课程签到情况",
            notes = "根据给定的课序号，获取这个课的所有学生的出席情况。这个出席情况是不与时间建立联系的")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cid",value = "所要查询的课序号",dataTypeClass = String.class,required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功返回对象。<br>" +
                    "code=0:data中返回一个List&lt;viewCourse&gt:，这个viewCourse对象中只注入了sid、sname、present<br>" +
                    "code=-1:课程id错误")
    })
    public Result<?> getPresent(@RequestParam(defaultValue = "-1") String cid){
        if(cid.equals("")){
            return Result.error("-1","错误的课程id");
        }
        List<viewCourse> answer = courseMapper.getPresent(cid);
        return Result.success(answer);
    }

    @PutMapping("/savePresent")
    @ApiOperation(value = "设置课程签到情况",
            notes = "输入一个viewCourse列表，根据这个列表进行更新")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "presents",value = "带有签到信息的列表",required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功返回对象。<br>" +
                    "code=0:返回无数据Result")
    })
    public Result<?> savePresent(@RequestBody List<viewCourse> presents){
        QueryWrapper wrapper = new QueryWrapper();
        currentchoose mid = new currentchoose();
        for(viewCourse i : presents){
            wrapper.clear();
            wrapper.eq("sid",i.getSid());
            wrapper.eq("cid",i.getCid());
            mid.setCid(i.getCid());
            mid.setSid(i.getSid());
            mid.setPresent(i.getPresent());
            currentchooseMapper.update(mid,wrapper);
        }
        return Result.success();
    }

    @PutMapping("/refreshPresent")
    @ApiOperation(value = "刷新某个课程的签到",
            notes = "刷新指定课程的签到信息，将所有人的签到设置为false")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cid",value = "要刷新的课序号",required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功返回对象。<br>" +
                    "code=0:返回无数据Result")
    })
    public Result<?> refreshPresent(@RequestBody String cid){

        currentchooseMapper.refreshPresent(cid);
        return  Result.success();
    }

    @PutMapping("/setScore")
    @ApiOperation(value = "设置成绩",
            notes = "根据给定的成绩设置信息对象，更新成绩设置")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "scoreInfo",value = "设置成绩的依据",dataTypeClass = setScoreInfo.class,required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功返回对象。<br>" +
                    "code=0:返回无数据Result<br>" +
                    "code=-1:给出的成绩设置信息对象不正常")
    })
    public Result<?> setScore(@RequestBody setScoreInfo scoreInfo){
        if(scoreInfo.getSid()==-1 || scoreInfo.getCid().equals("") || scoreInfo.getScore()==-1){
            return Result.error("-1","错误的学生选课信息");
        }
        else{
            int num = courseMapper.setScore(scoreInfo.getCid(),scoreInfo.getSid(),scoreInfo.getScore());
            Result answer = Result.success();
            answer.setMsg("更新了"+num+"个成绩");
            return answer;
        }
    }

    @PostMapping("/getRank")
    @ApiOperation(value = "获取排名",
            notes = "获取学生的某门课的排名信息，可以根据不同的typeCode获取不同范围的排名信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cid",value = "指定的课程id",dataTypeClass = String.class,required = true),
            @ApiImplicitParam(name = "typeCode",value = "根据typeCode的不同获取不同范围内的排名（默认以同年级为基础）<br>" +
                    "typeCode=0：获取同一门课所有人范围内的排名<br>" +
                    "typeCode=1：获取同一门课在同专业人范围内的排名<br>" +
                    "typeCode=2：获取同一门课同班人范围内的排名<br>" +
                    "其他typeCode会被认为错误"),
            @ApiImplicitParam(name = "stuInfo",value = "指定的学生信息，至少应该包括学生id、年级、专业、班级号")
    })
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功返回对象。<br>" +
                    "code=0:直接返回一个字符串，格式为 排名/总人数<br>" +
                    "code=-1:typeCode错误")
    })
    public Result<?> getRank(@RequestParam String cid,@RequestParam Integer typeCode,@RequestBody viewUser viewStuInfo){
        student stuInfo = viewStuInfo.toStudent();
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("grade",stuInfo.getGrade());
        if(typeCode==0){
            //占位使用
        }
        else if(typeCode==1){
            wrapper.eq("mid",stuInfo.getMid());
        }
        else if(typeCode==2){
            wrapper.eq("mid",stuInfo.getMid());
            wrapper.eq("classnum",stuInfo.getClassnum());
        }
        else{
            return Result.error("-1","typeCode错误");
        }
        Integer rank = courseMapper.getRank(wrapper,cid,stuInfo.getSid());
        wrapper.eq("cid",cid);
        Integer total = courseMapper.getSpecificTotal(wrapper);
        String answer = rank.toString()+"/"+total.toString();
        return Result.success(answer);
    }

    @PostMapping("/globalSet")
    @ApiOperation(value = "全局结算",
            notes = "进行全局结算，将所有的当前课程信息都转移到历史课程中，然后删除他们")
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功返回对象。<br>" +
                    "code=0:返回无数据Result")
    })
    public Result<?> globalSet(){
        currentsemester currentsemester = (currentsemester)getSEMESTER().getData();
        courseMapper.curCourseToHisCourse(currentsemester.getYear(),currentsemester.getSemester());
        courseMapper.curChooseToHisChoose(currentsemester.getYear(),currentsemester.getSemester());
        prechooseMapper.delete(null);
        courseMapper.delete(null);
        currentchooseMapper.delete(null);
        return Result.success();
    }

    @PostMapping("toCHOOSING")
    @ApiOperation(value = "开启选课",
            notes = "开启选课（将选课位设置为true），并将学期设置为传入的学期")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "updating",value = "指明这次选课开启所处的学期",dataTypeClass = currentsemester.class,required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功返回对象。<br>" +
                    "code=0:返回无数据Result")
    })
    public Result<?> toCHOOSING(@RequestBody currentsemester updating){
        flags flag = new flags();
        flag.setFlag(true);
        setCHOOSING(flag);
        setSEMESTER(updating);
        return Result.success();
    }

    @PostMapping("toUnCHOOSING")
    @ApiOperation(value = "关闭选课",
            notes = "关闭选课（将选课位设置为false）")
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功返回对象。<br>" +
                    "code=0:返回无数据Result")
    })
    public Result<?> toUnCHOOSING(){
        preToCurCourse();
        flags setting = new flags();
        setting.setFlag(false);
        setCHOOSING(setting);
        return Result.success();
    }
    public Result<?> preToCurCourse(){
        courseMapper.preTocurCourse();
        prechooseMapper.delete(null);
        return Result.success();
    }

    @DeleteMapping("deleteArrange")
    @ApiOperation(value = "删除安排",
            notes = "删除一个课程安排")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deleting",value = "所要删除安排",dataTypeClass = arrange.class,required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功返回对象。<br>" +
                    "code=0:返回无数据Result<br>" +
                    "code=-1:检测到删除了0条数据，删除行为是不正常的")
    })
    public Result<?> deleteArrange(@RequestBody arrange deleting){
        if(courseExit(deleting.getCid()).getCode().equals("0")){
            return Result.error("-1","此课程不存在");
        }
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("cid",deleting.getCid());
        wrapper.eq("weekday",deleting.getWeekday());
        wrapper.eq("dayorder",deleting.getDayorder());
        int deleteNum = arrangeMapper.delete(wrapper);
        if(deleteNum==0){
            return Result.error("-1","没有删除任何安排，建议检查");
        }
        return Result.success();
    }
    @PostMapping("insertArrange")
    @ApiOperation(value = "插入安排",
            notes = "插入一个课程安排")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "inserting",value = "所要插入的安排",dataTypeClass = arrange.class,required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功返回对象。<br>" +
                    "code=0:返回无数据Result<br>" +
                    "code=-1:要插入的课程安排所对应的课程并不存在<br>" +
                    "code=-2:所要插入的时间已有安排")
    })
    public Result<?> insertArrange(@RequestBody arrange inserting){
        if(courseExit(inserting.getCid()).getCode().equals("0")){
            return Result.error("-1","此课程不存在");
        }
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("cid",inserting.getCid());
        wrapper.eq("weekday",inserting.getWeekday());
        wrapper.eq("dayorder",inserting.getDayorder());
        if(!arrangeMapper.selectList(wrapper).isEmpty()){
            return Result.error("-2","此时间已有安排");
        }
        arrangeMapper.insert(inserting);
        return Result.success();
    }
    @PutMapping("updateArrange")
    @ApiOperation(value = "更新安排",
            notes = "更新一个课程安排")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "updating",value = "所要更新的安排",dataTypeClass = arrange.class,required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功返回对象。<br>" +
                    "code=0:返回无数据Result<br>" +
                    "code=-1:要更新的课程安排所对应的课程并不存在<br>" +
                    "code=-2:没有更新任何安排，更新行为是不正常的")
    })
    public Result<?> updateArrange(@RequestBody arrange updating){
        if(courseExit(updating.getCid()).getCode().equals("0")){
            return Result.error("-1","此课程不存在");
        }
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("cid",updating.getCid());
        wrapper.eq("weekday",updating.getWeekday());
        wrapper.eq("dayorder",updating.getDayorder());
        int updateNum = arrangeMapper.update(updating,wrapper);
        if(updateNum == 0){
            return Result.error("-2","被更新的安排不存在");
        }
        return Result.success();
    }

    @GetMapping("/getCoursePrecise")
    @ApiOperation(value = "获取课程",
            notes = "获取单个课程信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cid",value = "所搜索课程的课序号",dataTypeClass = String.class,required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功返回对象。<br>" +
                    "code=0:data中返回course对象，course对象参见models" +
                    "code=-1:输入的课序号异常<br>" +
                    "code=-2:找不到查找的课程")
    })
    public Result<?> getCourse(@RequestParam(defaultValue = "-1")String cid){
        if(cid.equals("-1")){
            return Result.error("-1","错误的课序号");
        }
        course answer = courseMapper.selectById(cid);
        if(answer==null){
            return Result.error("-2","找不到课程");
        }
        return Result.success(answer);
    }
    @DeleteMapping("deleteCourse")
    @ApiOperation(value = "删除课程",
            notes = "删除一个当前课程和它的安排")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deletingCid",value = "所要删除的课程的课序号",dataTypeClass = String.class,required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功返回对象。<br>" +
                    "code=0:返回无数据Result<br>" +
                    "code=-1:检测到删除了0条数据，删除行为是不正常的")
    })
    public Result<?> deleteCourse(@RequestBody String deletingCid){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("cid",deletingCid);
        arrangeMapper.delete(wrapper);
        int deleteNum = courseMapper.delete(wrapper);
        if(deleteNum==0){
            return Result.error("-1","没有删除任何课程，建议检查");
        }
        return Result.success();
    }
    @PostMapping("insertCourse")
    @ApiOperation(value = "插入课程",
            notes = "插入一个课程信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "inserting",value = "所要插入的课程信息",dataTypeClass = course.class,required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功返回对象。<br>" +
                    "code=0:返回无数据Result<br>" +
                    "code=-1:想要插入的课程课序号已经存在")
    })
    public Result<?> insertCourse(@RequestBody course inserting){
        if(courseExit(inserting.getCid()).getCode().equals("1")){
            return Result.error("-1","此课序号已被占用");
        }
        courseMapper.insert(inserting);
        return Result.success();
    }
    @PutMapping("updateCourse")
    @ApiOperation(value = "更新课程信息",
            notes = "更新一个课程的基础信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "updating",value = "所要更新的课程信息",dataTypeClass = course.class,required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功返回对象。<br>" +
                    "code=0:返回无数据Result<br>" +
                    "code=-1:要更新的课程并不存在<br>" +
                    "code=-2:更新后发现被更新的课程并不存在")
    })
    public Result<?> updateCourse(@RequestBody course updating){
        if(courseExit(updating.getCid()).getCode().equals("0")){
            return Result.error("-1","此课程不存在");
        }
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("cid",updating.getCid());
        int updateNum = courseMapper.updateCourse(updating.getCname(),updating.getTid(),updating.getTotal(),wrapper);
        if(updateNum == 0){
            return Result.error("-2","被更新的课程不存在");
        }
        return Result.success();
    }


    @GetMapping("courseExit")
    @ApiOperation(value = "课程存在确认",
        notes = "给定课序号，返回该课序号的课程是否存在")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "cid",value = "所查询的课序号",dataTypeClass = String.class,required = true)
    })
    @ApiResponses({
        @ApiResponse(code = 200,message = "成功返回对象。<br>" +
                "code=0:指定课序号的课程不存在<br>" +
                "code=1:指定课序号的课程存在")
    })
    public Result<?> courseExit(@RequestParam String cid){
        course check = courseMapper.selectById(cid);
        Result answer = Result.success();
        if(check==null){
            answer.setCode("0");
        }
        else{
            answer.setCode("1");
        }
        return answer;
    }

    @GetMapping("getCHOOSING")
    @ApiOperation(value = "获取CHOOSING",
            notes = "获取当前是否处于选课阶段，CHOOSING是这一状态的表征")
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功返回对象。<br>" +
                    "code=0:data中返回flags对象，flags参见models")
    })
    public Result<?> getCHOOSING(){
        flags answer = flagsMapper.selectById("CHOOSING");
        return Result.success(answer);
    }
    @PostMapping("setCHOOSING")
    @ApiOperation(value = "设定CHOOSING",
        notes = "设定当前是否处于选课阶段，CHOOSING是这一状态的表征")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "updating",value = "包含着CHOOSING状态的flags对象，请参见models",dataTypeClass = flags.class,required = true)
    })
    @ApiResponses({
        @ApiResponse(code = 200,message = "成功返回对象。<br>" +
                "code=0:返回无数据Result")
    })
    public Result<?> setCHOOSING(@RequestBody flags updating){
        updating.setEvent("CHOOSING");
        flagsMapper.updateById(updating);
        return Result.success();
    }
    @GetMapping("getSEMESTER")
    @ApiOperation(value = "获取学期信息",
            notes = "获取当前所处的学期")
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功返回对象。<br>" +
                    "code=0:data中返回一个List&lt;currentsemester&gt;，currentsemester参见models")
    })
    public Result<?> getSEMESTER(){
        List<currentsemester> answer_list = courseMapper.getSEMESTER();
        answer_list.get(0).setYear(answer_list.get(0).getYear().split("-")[0]);
        return Result.success(answer_list.get(0));
    }
    @PostMapping("setSEMESTER")
    @ApiOperation(value = "设定学期",
            notes = "设定当前系统学期")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "updating",value = "包含学期信息的currentsemester对象，请参见models",dataTypeClass = currentsemester.class,required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功返回对象。<br>" +
                    "code=0:返回无数据Result")
    })
    public Result<?> setSEMESTER(@RequestBody currentsemester updating){
        courseMapper.setSEMESTER(updating.getYear(),updating.getSemester());
        return Result.success();
    }
}

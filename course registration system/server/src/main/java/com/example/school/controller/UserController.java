package com.example.school.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.school.common.Result;
import com.example.school.entity.baseEntity.*;
import com.example.school.entity.complexEntity.viewUser;
import com.example.school.mapper.*;
import com.example.school.utils.JWTUtils;
import io.swagger.annotations.*;
import io.swagger.models.auth.In;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@ApiOperation(value = "",
//        notes = "")
//@ApiImplicitParams({
//        @ApiImplicitParam(name = "",value = "",dataTypeClass = ,required = true)
//})
//@ApiResponses({
//        @ApiResponse(code = 200,message = "成功返回对象。<br>" +
//                "code=0:")
//})

@Api(tags = "用户信息操作")
@RestController
@RequestMapping("/user")
@Transactional
public class UserController {
    @Resource
    rootMapper rootMapper;
    @Resource
    studentMapper studentMapper;
    @Resource
    teacherMapper teacherMapper;
    @Resource
    allclassMapper allclassMapper;
    @Resource
    majorMapper majorMapper;


    @PostMapping("login")
    @ApiOperation(value = "登录",
        notes = "验证一份尝试登录的用户信息。")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "user",value = "一个登录中用户的信息，我们必须信息是id、password、role",dataTypeClass = viewUser.class,required = true)
    })
    @ApiResponses({
        @ApiResponse(code = 200,message = "成功返回对象。<br>" +
                "code=0:data中返回token，前端应将这个token存储，并在登录后的任何请求操作的头部使用一个属性名为token的字段回传它。" +
                "这是用户的验证信息，不发送正确的token或超时会导致验证错误，回传一个Result对象，根据code不同表征不同的验证错误原因:<br>" +
                "code=-435:请求头中无token内容<br>" +
                "code=-436:签名无效（极可能是被篡改）<br>" +
                "code=-437:登录过期<br>" +
                "code=-438:后台加密算法不一致" +
                "code=-439:未知错误")
    })
    public Result<?> login(@RequestBody viewUser user){
        QueryWrapper wrapper = new QueryWrapper();
        user checked;
        if(user.getRole().equals("student")){
            wrapper.eq("sid",user.getId());
            wrapper.eq("password",user.getPassword());
            checked = studentMapper.selectOne(wrapper);
        }
        else if(user.getRole().equals("teacher")){
            wrapper.eq("tid",user.getId());
            wrapper.eq("password",user.getPassword());
            checked = teacherMapper.selectOne(wrapper);
        }
        else if(user.getRole().equals("root")){
            wrapper.eq("rid",user.getId());
            wrapper.eq("password",user.getPassword());
            checked = rootMapper.selectOne(wrapper);
        }
        else{
            return Result.error("-1","未知的用户角色");
        }
        if(checked!=null){
            viewUser res = checked.toViewUser();
            Map<String,String> payload = new HashMap<>();
            payload.put("id",res.getId().toString());
            payload.put("name",res.getName());
            payload.put("role",res.getRole());
            String token = JWTUtils.getToken(payload);
            return Result.success(token);
        }
        return Result.error("-2","错误的id与密码");
    }

    @GetMapping("getUser")
    @ApiOperation(value = "获取用户信息",
        notes = "给定用户id和角色，返回用户信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id",value = "用户id",dataTypeClass = Integer.class,required = true),
        @ApiImplicitParam(name = "role",value = "用户角色",dataTypeClass = String.class,required = true,allowableValues = "student,teacher,root")
    })
    @ApiResponses({
        @ApiResponse(code = 200,message = "成功返回对象。<br>" +
                "code=0:data中返回所需的viewUser对象，viewUser参见models<br>" +
                "code=-1:role角色未知，超出可选范围")
    })
    public Result<?> getUser(@RequestParam Integer id,@RequestParam String role){
        if(role.equals("student")){
            return Result.success(studentMapper.selectById(id).toViewUser());
        }
        else if(role.equals("teacher")){
            return Result.success(teacherMapper.selectById(id).toViewUser());
        }
        else if(role.equals("root")){
            return Result.success(rootMapper.selectById(id).toViewUser());
        }
        else{
            return Result.error("-1","未知角色");
        }
    }

    @PutMapping("change")
    @ApiOperation(value = "更改用户信息",
        notes = "给出一个用户信息，根据用户id和角色进行更新")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "user",value = "指明用户信息的viewUser对象，viewUser参见models",dataTypeClass = viewUser.class,required = true)
    })
    @ApiResponses({
        @ApiResponse(code = 200,message = "成功返回对象。<br>" +
                "code=0:data中返回一个新的token以适应新的用户信息，token相关信息可以参见登录接口<br>" +
                "code=-1:角色未知")
    })
    public Result<?> change(@RequestBody viewUser user){
        if(user.getRole().equals("student")){
            studentMapper.updateById(user.toStudent());
        }
        else if(user.getRole().equals("teacher")){
            teacherMapper.updateById(user.toTeacher());
        }
        else if(user.getRole().equals("root")){
            rootMapper.updateById(user.toRoot());
        }
        else{
            return Result.error("-1","未知角色");
        }
        Map<String,String> payload = new HashMap<>();
        payload.put("id",user.getId().toString());
        payload.put("name",user.getName());
        payload.put("role",user.getRole());
        String token = JWTUtils.getToken(payload);
        return Result.success(token);
    }

    @PostMapping("register")
    @ApiOperation(value = "注册",
            notes = "用户注册，将用户信息录入")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user",value = "指明用户信息的viewUser对象，viewUser参见models",dataTypeClass = viewUser.class,required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功返回对象。<br>" +
                    "code=0:data中返回注册生成的id（用户要依赖它登录）<br>" +
                    "code=-1:角色未知<br>" +
                    "code=-2:电话已经被使用，我们不允许电话重复")
    })
    public Result<?> register(@RequestBody viewUser user){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("phone",user.getPhone());
        List<?> check;
        if(user.getRole().equals("student")){
            check = studentMapper.selectList(wrapper);
        }
        else if(user.getRole().equals("teacher")){
            check = teacherMapper.selectList(wrapper);
        }
        else if(user.getRole().equals("root")){
            check = rootMapper.selectList(wrapper);
        }
        else{
            return Result.error("-1","未知角色");
        }
        if(!check.isEmpty()){
            return Result.error("-2","电话已被使用");
        }
        if(user.getRole().equals("student")){
            student inserting = user.toStudent();
            studentMapper.insert(inserting);
            return Result.success(inserting.getSid());
        }
        else if(user.getRole().equals("teacher")){
            teacher inserting = user.toTeacher();
            teacherMapper.insert(inserting);
            return Result.success(inserting.getTid());
        }
        else if(user.getRole().equals("root")){
            root inserting = user.toRoot();
            rootMapper.insert(inserting);
            return Result.success(inserting.getRid());
        }
        else{
            return Result.error("-1","未知角色");
        }

    }

    @GetMapping("getMajorList")
    @ApiOperation(value = "获取专业列表",
            notes = "获取专业列表")
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功返回对象。<br>" +
                    "code=0:data中返回一个专业列表")
    })
    public Result<?> getMajorList(){
        List<major> answer = majorMapper.selectList(null);
        return Result.success(answer);
    }

    @GetMapping("classCheck")
    @ApiOperation(value = "班级检查",
            notes = "根据输入的班级信息，检查该班级是否存在")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mid",value = "所查班级的专业id",dataTypeClass = Integer.class,required = true),
            @ApiImplicitParam(name = "grade",value = "所查班级的年级",dataTypeClass = String.class,required = true),
            @ApiImplicitParam(name = "classnum",value = "所查班级的班级号",dataTypeClass = Integer.class,required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功返回对象。<br>" +
                    "code=0:该班级存在<br>" +
                    "code=-1，该班级不存在")
    })
    public Result<?> classCheck(@RequestParam Integer mid,
                                @RequestParam String grade,
                                @RequestParam Integer classnum){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("mid",mid);
        wrapper.eq("grade",grade);
        wrapper.eq("classnum",classnum);
        List<allclass> answer = allclassMapper.selectList(wrapper);
        if(answer.isEmpty()){
            return Result.error("-1","该班级不存在，请检查");
        }
        else{
            return Result.success();
        }
    }
}

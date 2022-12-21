package com.example.school.entity.complexEntity;

import com.example.school.entity.baseEntity.root;
import com.example.school.entity.baseEntity.student;
import com.example.school.entity.baseEntity.teacher;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "viewUser",
        description = "用于通用user，由于不同的角色允许拥有相同的id，所以必须使用role加以区分。可向三种具体的用户信息转换。")
public class viewUser {
    private Integer id;
    private String name;
    private String birthday;
    private String phone;
    private String password;
    private String email;
    private String address;
    private Integer mid;
    private String grade;
    private Integer classnum;
    @ApiModelProperty(value = "角色",required = true,allowableValues = "student,teacher,root")
    private String role;
    public student toStudent(){
        if(!role.equals("student")){
            return null;
        }
        else{
            student answer = new student();
            answer.setSid(id);
            answer.setSname(name);
            answer.setBirthday(birthday);
            answer.setPhone(phone);
            answer.setPassword(password);
            answer.setEmail(email);
            answer.setAddress(address);
            answer.setMid(mid);
            answer.setGrade(grade);
            answer.setClassnum(classnum);
            return answer;
        }
    }

    public teacher toTeacher(){
        if(!role.equals("teacher")){
            return null;
        }
        else{
            teacher answer = new teacher();
            answer.setTid(id);
            answer.setTname(name);
            answer.setBirthday(birthday);
            answer.setPhone(phone);
            answer.setPassword(password);
            answer.setEmail(email);
            return answer;
        }
    }

    public root toRoot(){
        if(!role.equals("root")){
            return null;
        }
        else{
            root answer = new root();
            answer.setRid(id);
            answer.setRname(name);
            answer.setBirthday(birthday);
            answer.setPhone(phone);
            answer.setPassword(password);
            answer.setEmail(email);
            return answer;
        }
    }
}

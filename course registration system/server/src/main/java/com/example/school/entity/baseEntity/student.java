package com.example.school.entity.baseEntity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.school.entity.complexEntity.viewUser;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@TableName("student")
@ApiModel(value = "student",
        description = "学生用户信息")
public class student implements user {
    @TableId(value = "sid",type= IdType.AUTO)
    private Integer sid;
    private String sname;
    private String birthday;
    private String phone;
    private String password;
    private String email;
    private String address;
    private Integer mid;
    private String grade;
    private Integer classnum;

    @Override
    public viewUser toViewUser() {
        viewUser answer = new viewUser();
        answer.setId(sid);
        answer.setName(sname);
        answer.setBirthday(birthday);
        answer.setPhone(phone);
        answer.setPassword(password);
        answer.setEmail(email);
        answer.setAddress(address);
        answer.setMid(mid);
        answer.setGrade(grade);
        answer.setClassnum(classnum);
        answer.setRole("student");
        return answer;
    }
}

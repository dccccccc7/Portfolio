package com.example.school.entity.baseEntity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.school.entity.complexEntity.viewUser;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@TableName("teacher")
@ApiModel(value = "teacher",
        description = "教师用户信息")
public class teacher implements user{
    @TableId(value="tid",type= IdType.AUTO)
    private Integer tid;
    private String tname;
    private String birthday;
    private String phone;
    private String password;
    private String email;

    @Override
    public viewUser toViewUser() {
        viewUser answer = new viewUser();
        answer.setId(tid);
        answer.setName(tname);
        answer.setBirthday(birthday);
        answer.setPhone(phone);
        answer.setPassword(password);
        answer.setEmail(email);
        answer.setRole("teacher");
        return answer;
    }
}

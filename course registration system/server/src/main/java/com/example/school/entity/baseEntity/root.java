package com.example.school.entity.baseEntity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.school.entity.complexEntity.viewUser;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@TableName("root")
@ApiModel(value = "root",
        description = "根用户信息")
public class root implements user{
    @TableId(value="rid",type= IdType.AUTO)
    private Integer rid;
    private String rname;
    private String birthday;
    private String phone;
    private String password;
    private String email;

    @Override
    public viewUser toViewUser() {
        viewUser answer = new viewUser();
        answer.setId(rid);
        answer.setName(rname);
        answer.setBirthday(birthday);
        answer.setPhone(phone);
        answer.setPassword(password);
        answer.setEmail(email);
        answer.setRole("root");
        return answer;
    }
}

package com.example.school.entity.baseEntity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@TableName("major")
@Data
@ApiModel(value = "major",
        description = "对应数据库表major，单个对象存储着一个专业信息")
public class major {
    private Integer mid;
    private String mname;
}

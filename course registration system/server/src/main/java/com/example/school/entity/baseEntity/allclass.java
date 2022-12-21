package com.example.school.entity.baseEntity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@TableName("allclass")
@Data
@ApiModel(value = "allclass",
        description = "对应数据库表allclass，单个对象存储着一个现存班级的信息")
public class allclass {
    private Integer mid;
    private String grade;
    private Integer classnum;
}

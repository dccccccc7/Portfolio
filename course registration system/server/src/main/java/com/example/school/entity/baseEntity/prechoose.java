package com.example.school.entity.baseEntity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@TableName("prechoose")
@ApiModel(value = "prechoose",
        description = "(来自数据库表)" +
                "预选阶段的单条选课信息，专用于选课阶段的选课操作，一个对象是一个学生和一个课程的选课关系")
public class prechoose {
    @ApiModelProperty(value = "学生id",required = true,example = "1")
    private Integer sid;
    @ApiModelProperty(value = "课程id",required = true,example = "101")
    private String cid;
}

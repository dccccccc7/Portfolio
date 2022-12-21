package com.example.school.entity.baseEntity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@TableName("currentchoose")
@ApiModel(value = "currentchoose",
        description = "(来自数据库表)" +
                "正式上课阶段的单条选课信息，专用于正式上课阶段的信息传输，一个对象包括学生与课程的关系以及对应分数")
public class currentchoose {
    @ApiModelProperty(value = "学生id",required = true,example = "1")
    private Integer sid;
    @ApiModelProperty(value = "课序号",required = true,example = "101")
    private String cid;
    @ApiModelProperty(value = "成绩",required = true,example = "59")
    private Integer score;
    @ApiModelProperty(value = "签到",required = false,example = "true")
    private Boolean present;
}

package com.example.school.entity.complexEntity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "setScoreInfo",
        description = "单个成绩信息，每个对象存储了某个学生的某个课程的成绩")
public class setScoreInfo {
    @ApiModelProperty(value = "学生id",required = true,example = "1")
    private Integer sid;
    @ApiModelProperty(value = "课序号",required = true,example = "101")
    private String cid;
    @ApiModelProperty(value = "分数",required = true,example = "59")
    private Integer score;
}

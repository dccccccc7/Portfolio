package com.example.school.entity.complexEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "viewTableBlock",
        description = "用于为课表传输数据，一个对象课表中的单个单元格中的信息")
public class viewTableBlock {
    @ApiModelProperty(value = "课序号",required = true,example = "101")
    private String cid;
    @ApiModelProperty(value = "课程名",required = true,example = "离散数学")
    private String cname;
    @ApiModelProperty(value = "教师id",required = true,example = "1")
    private Integer tid;
    @ApiModelProperty(value = "教师姓名",required = true,example = "潘")
    private String tname;
    @ApiModelProperty(value = "上课日期(周X)",required = true,example = "3")
    private Integer weekday;
    @ApiModelProperty(value = "上课节次",required = true,example = "1")
    private Integer dayorder;
    @ApiModelProperty(value = "上课地点",example = "6区107")
    private String place;
    @ApiModelProperty(value = "起始周",required = true,example = "1")
    private Integer beginWeek;
    @ApiModelProperty(value = "终止周",required = true,example = "17")
    private Integer endWeek;
    @ApiModelProperty(value = "成绩",required = true,example = "59")
    private Integer score;
    @ApiModelProperty(value = "学分",required = true,example = "2.0")
    private Double credit;
}

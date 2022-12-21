package com.example.school.entity.complexEntity;

import com.example.school.entity.baseEntity.arrange;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "viewCourse",
        description = "某一个课程最详细的信息。它将各种角色的id扩充，附加名，同时使用一个数组存储单个课程的全部安排。同时也添加了学生相关字段")
public class viewCourse {
    @ApiModelProperty(value = "课序号",example = "101")
    private String cid;
    @ApiModelProperty(value = "课程名",example = "离散数学")
    private String cname;
    @ApiModelProperty(value = "教师id",example = "1")
    private Integer tid;
    @ApiModelProperty(value = "教师姓名",example = "潘")
    private String tname;
    @ApiModelProperty(value = "学生id",example = "1")
    private Integer sid;
    @ApiModelProperty(value = "学生姓名",example = "张")
    private String sname;
    @ApiModelProperty(value = "课程已选人数",example = "3")
    private Integer num;
    @ApiModelProperty(value = "课程人数总容纳量",example = "100")
    private Integer total;
    @ApiModelProperty(value = "实际课余量",example = "97")
    private Integer left;
    @ApiModelProperty(value = "成绩",example = "59")
    private Integer score;
    @ApiModelProperty(value = "课程描述",example = "这是一门好课程，朋友")
    private String describes;
    @ApiModelProperty(value = "学分",example = "1.5")
    private Double credit;
    @ApiModelProperty(value = "签到",example = "true")
    private Boolean present;
    @ApiModelProperty(value = "安排列表，单条安排内容可以参考arranges类")
    private List<arrange> arranges;
}

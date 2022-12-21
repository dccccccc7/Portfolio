package com.example.school.entity.baseEntity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("historycourse")
@ApiModel(value = "historycourse",
        description = "(来自数据库表)" +
                "单个历史课程信息，这个课程已经结课了，留作历史备份")
public class historycourse {
    @ApiModelProperty(value = "课序号",required = true,example = "101")
    private String cid;
    @ApiModelProperty(value = "课程名",required = true,example = "离散数学")
    private String cname;
    @ApiModelProperty(value = "曾教此课的教师id",required = true,example = "1")
    private Integer tid;
    @ApiModelProperty(value = "曾教此课的教师姓名",required = true,example = "潘")
    private String tname;
    @ApiModelProperty(value = "此课开设的学年（春季学期的课此变量应为上一年）",required = true,example = "2020")
    private String year;
    @ApiModelProperty(value = "此课开设的学期",required = true,example = "1")
    private Integer semester;
    @ApiModelProperty(value = "对课程的描述",required = true,example = "这是一门好课")
    private String describes;
    @ApiModelProperty(value = "学分",required = true,example = "1.5")
    private Double credit;
    @TableField(exist = false)
    private Integer score;
}

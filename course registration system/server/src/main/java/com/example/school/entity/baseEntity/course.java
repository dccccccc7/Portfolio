package com.example.school.entity.baseEntity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("course")
@ApiModel(value = "course",
        description = "(来自数据库表)" +
                "单个现有课程的信息")
public class course implements Serializable {
    private static final long serialVersionUID = 42L;
    @TableId(value="cid",type= IdType.INPUT)
    @ApiModelProperty(value = "课序号",required = true,example = "101")
    private String cid;
    @ApiModelProperty(value = "课程名",required = true,example = "离散数学")
    private String cname;
    @ApiModelProperty(value = "授课教师id",required = true)
    private Integer tid;
    @ApiModelProperty(value = "课程已选人数",required = true)
    private Integer num;
    @ApiModelProperty(value = "课程人数总容纳量",required = true)
    private Integer total;
    @ApiModelProperty(value = "对课程的描述",required = true)
    private String describes;
    @ApiModelProperty(value = "学分",required = true)
    private Double credit;
}

package com.example.school.entity.baseEntity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("arrange")
@ApiModel(value = "arrange",
        description = "(来自数据库表)" +
                "某一个课程的单条上课安排信息，单个arrange对象代表了某一课程在某一天的某一大节有课程，并记录地点，上课起止周")
public class arrange {
    @ApiModelProperty(value = "课序号",required = true,example = "101")
    private String cid;
    @ApiModelProperty(value = "课程所处日期（星期X）",required = true,example = "3")
    private Integer weekday;
    @ApiModelProperty(value = "课程节次",required = true,example = "1")
    private Integer dayorder;
    @ApiModelProperty(value = "上课地点",example = "4区107")
    private String place;
    @ApiModelProperty(value = "起始周",required = true,example = "1")
    private Integer beginWeek;
    @ApiModelProperty(value = "终止周",required = true,example = "17")
    private Integer endWeek;
}

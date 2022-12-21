package com.example.school.entity.baseEntity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.HashMap;

@Data
@TableName("currentsemester")
@ApiModel(value = "currentsemester",
        description = "(来自数据库表)" +
                "用途特殊,来自于数据库中专门记录当下所处的学年学期的表")
public class currentsemester {
    @ApiModelProperty(value = "当前学年（公元纪年）",required = true,example = "2020")
    private String year;
    @ApiModelProperty(value = "所处学期，一般是1或2，但我们允许其他值",required = true,example = "1")
    private Integer semester;
}

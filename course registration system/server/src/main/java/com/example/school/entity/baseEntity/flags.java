package com.example.school.entity.baseEntity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("flags")
@ApiModel(value = "flags",
        description = "(来自数据库表)" +
                "用途特殊，一般用于指明某个事件的状态，这个状态一定是二元的")
public class flags {
    @TableId(value = "event",type = IdType.INPUT)
    @ApiModelProperty(value = "特定的事件",required = true,example = "CHOOSING")
    private String event;
    @ApiModelProperty(value = "表征状态的flag",required = true,example = "true")
    private boolean flag;
}

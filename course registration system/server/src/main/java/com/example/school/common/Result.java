package com.example.school.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Result<T>",
        description = "统一的接口返回值类，所有接口都使用Result类作为返回值返回前段")
public class Result<T> {
    @ApiModelProperty(value = "用于指明本次请求结果是否正确，我们约定为'0'时为成功，否则为失败。可以根据code的内容与错误进行对应。",required = true,example = "0")
    private String code;//为0时代表成功，不为0则为失败
    @ApiModelProperty(value = "请求结果的附加信息。" +
            "这里可以指明返回的Result所带有的状态，解释当前的请求成功或发生了什么样的错误",example = "这个对象是示例")
    private String msg;
    @ApiModelProperty(value = "请求结果的数据主体。" +
            "可以在不同的接口的响应状态状态码200的说明中确定data的具体类型",example = "{模板类型对象}")
    private T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Result() {
    }

    public Result(T data) {
        this.data = data;
    }

    //这是一些预置的快速构成方法，可以立刻返回指定要求的result对象，比如成功的或者失败的
    public static Result success() {
        Result result = new Result<>();
        result.setCode("0");
        result.setMsg("成功");
        return result;
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>(data);
        result.setCode("0");
        result.setMsg("成功");
        return result;
    }

    public static Result error(String code, String msg) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}

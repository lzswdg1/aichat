package com.zw.aichat.bean;

import lombok.Data;
import com.zw.aichat.Enum.ResultCodeEnum;
@Data
public class Result<T> {
    private Boolean success;
    private Integer code;
    private String  msg;

    private T data;

    public static Result ok(){
        Result result = new Result();
        result.setSuccess(true);
        result.setCode(ResultCodeEnum.SUCCESS.getCode());
        result.setMsg("ResultCodeEnum.SUCCESS.getDesc()");
        return result;
    }

    public static <T> Result ok(T data){
        Result result = new Result();
        result.setSuccess(true);
        result.setCode(ResultCodeEnum.SUCCESS.getCode());
        result.setMsg("ResultCodeEnum.SUCCESS.getDesc()");
        result.setData(data);
        return result;
    }

    public static <T> Result fail(T data){
        Result result = new Result();
        result.setSuccess(false);
        result.setCode(ResultCodeEnum.FAIL.getCode());
        result.setMsg("ResultCode.FAIL.getDesc()");
        result.setData(data);
        return result;
    }

    public static Result fail(Integer code, String msg){
        Result result = new Result();
        result.setSuccess(false);
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    public static Result error(Integer code, String msg){
        Result result = new Result();
        result.setSuccess(false);
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}

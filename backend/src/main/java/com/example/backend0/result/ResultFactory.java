package com.example.backend0.result;

/**
 * @ClassName ResultFactory
 * @Description
 **/
public class ResultFactory {
    public static Result buildResult(ResultCode resultCode, String message, Object backendReturn) {
        return new Result(resultCode.getCode(), message, backendReturn);
    }
    public static Result buildSuccessResult() {
        return buildResult(ResultCode.SUCCESS, "success", null);
    }
    public static Result buildSuccessResult(Object backendReturn) {
        return buildResult(ResultCode.SUCCESS, "success", backendReturn);
    }
    public static Result buildSuccessResult(String message, Object backendReturn) {
        return buildResult(ResultCode.SUCCESS, message, backendReturn);
    }
    public static Result buildSuccessResult(String message) {
        return buildResult(ResultCode.SUCCESS, message, null);
    }
    public static Result buildFailedResult(String message) {
        return buildResult(ResultCode.FAIL, message, null);
    }
    public static Result buildIllegalTokenResult(){return buildResult(ResultCode.ILLEGAL_TOKEN,null,null);}
    public static Result buildFailedResult(String message,Object backendReturn){return buildResult(ResultCode.FAIL, message, backendReturn);}
}

package com.example.backend0.result;

import lombok.Data;

/**
 * @ClassName Result
 * @Description 接口返回类
 **/
@Data
public class Result {
    private int code;
    private String message;
    private Object contents;


    public Result(int code, String message, Object object) {
        this.code = code;
        this.message = message;
        this.contents = object;
    }

}

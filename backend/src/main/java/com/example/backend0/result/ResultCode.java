package com.example.backend0.result;

/**
 * @ClassName ResultCode
 * @Description 返回值的code含义
 **/
public enum ResultCode {
    /**
     * 成功
     */
    SUCCESS(200),
    /**
     * 获取数据失败
     */
    FAIL(400),
    /**
     * 没有权限
     */
    UNAUTHORIZED(401),
    NOT_FOUND(404),
    /**
     * 内部服务器错误
     */
    INTERNAL_SERVER_ERROR(500),
    /**
     *  token错误
     */
    ILLEGAL_TOKEN(405);
    private final int code;

    ResultCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

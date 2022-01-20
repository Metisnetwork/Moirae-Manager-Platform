package com.moirae.rosettaflow.common.enums;

/**
 * 返回码定义
 *
 * @author huma
 * @date 2021/5/9
 */
public enum RespCodeEnum {
    /**
     * 成功
     */
    SUCCESS(10000),

    /**
     * 参数类型错误
     */
    PARAM_TYPE_ERROR(20001),

    /**
     * 请求方式错误
     */
    REQUEST_METHOD_ERROR(20002),

    /**
     * 参数格式错误
     */
    PARAM_FORMAT_ERROR(20003),

    /**
     * 请求参数错误
     */
    PARAM_ERROR(20004),

    /**
     * 用户未登录
     */
    UN_LOGIN(20006),

    /**
     * token invalid
     */
    TOKEN_INVALID(20007),

    /**
     * user not exist
     */
    USER_NOT_EXIST(20008),

    /**
     * 业务失败
     */
    BIZ_FAILED(20009),

    /**
     * 系统异常
     */
    EXCEPTION(30000),

    /**
     * 业务异常
     */
    BIZ_EXCEPTION(30001),

    /**
     * nonce invalid
     */
    NONCE_INVALID(30002);

    private final int code;

    RespCodeEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

package com.moirae.rosettaflow.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.moirae.rosettaflow.common.enums.ErrorMsg;
import com.moirae.rosettaflow.common.enums.RespCodeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author admin
 * @date 2021/7/20
 */
@Data
@ApiModel
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseVo<T> {
    @ApiModelProperty(value = "返回码", required = true)
    private final int code;

    @ApiModelProperty(value = "返回描述", required = true)
    private final String msg;

    @ApiModelProperty(value = "返回结果")
    private T data;

    private ResponseVo(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private ResponseVo(int code, String msg, T data) {
        this(code, msg);
        this.data = data;
    }

    private ResponseVo(RespCodeEnum respCodeEnum, String msg) {
        this(respCodeEnum.getCode(), msg);
    }

    private ResponseVo(RespCodeEnum respCodeEnum, String msg, T data) {
        this(respCodeEnum.getCode(), msg, data);
    }

    public static <T> ResponseVo<T> createSuccess() {
        return new ResponseVo<>(RespCodeEnum.SUCCESS, ErrorMsg.SUCCESS.getMsg());
    }

    public static <T> ResponseVo<T> createSuccess(T data) {
        return new ResponseVo<>(RespCodeEnum.SUCCESS, ErrorMsg.SUCCESS.getMsg(),data);
    }

    public static <T> ResponseVo<T> create(RespCodeEnum respCodeEnum, String msg) {
        return new ResponseVo<>(respCodeEnum, msg);
    }

    public static <T> ResponseVo<T> create(int code, String msg) {
        return new ResponseVo<>(code, msg);
    }

}

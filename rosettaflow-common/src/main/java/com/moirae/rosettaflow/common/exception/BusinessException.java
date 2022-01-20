package com.moirae.rosettaflow.common.exception;

import com.moirae.rosettaflow.common.enums.RespCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author admin
 * @date 2021/7/20
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessException extends RuntimeException {

    private int code;
    private String msg;

    public BusinessException() {
        super();
    }

    public BusinessException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public BusinessException(RespCodeEnum code, String msg) {
        super(msg);
        this.code = code.getCode();
        this.msg = msg;
    }
}

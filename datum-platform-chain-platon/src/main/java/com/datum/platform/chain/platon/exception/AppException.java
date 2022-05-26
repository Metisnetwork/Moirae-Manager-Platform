package com.datum.platform.chain.platon.exception;

import com.datum.platform.chain.platon.enums.CodeEnum;

/**
 * 基础异常定义
 */
public class AppException extends RuntimeException {

	private static final long serialVersionUID = 8204561001063070964L;

	private CodeEnum code;

	public AppException(CodeEnum code, String msg){
        super(msg);
        this.code = code;
    }

    public AppException(CodeEnum code, String msg, Throwable throwable){
        super(msg,throwable);
        this.code = code;
    }
}

package com.platon.rosettaflow.common.exception;

import lombok.Getter;

/**
 * @author admin
 * @date 2021/7/20
 */
public class ApplicationException extends RuntimeException{

    @Getter
    private String errorMsg;

    public ApplicationException(String errorMsg){
        super(errorMsg);
        this.errorMsg = errorMsg;
    }

    public ApplicationException(Throwable exception){
        super(exception);
        this.errorMsg = exception.getMessage();
    }

    public ApplicationException(String errorMsg, Throwable exception){
        super(exception);
        this.errorMsg = errorMsg;
    }
}

package com.moirae.rosettaflow.config;

import com.moirae.rosettaflow.common.enums.ErrorMsg;
import com.moirae.rosettaflow.common.enums.RespCodeEnum;
import com.moirae.rosettaflow.common.exception.BusinessException;
import com.moirae.rosettaflow.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * @author admin
 * @date 2021/7/20
 */
@Slf4j
@RestControllerAdvice
public class ControllerAdvice {
    static final String FILE_SIZE_OUT_LIMIT = "上传文件大小超出限制!";

    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public <T> ResponseVo<T> exceptionHandler(Exception e) {
        log.error("An exception occurred on the request.", e);
        return ResponseVo.create(RespCodeEnum.EXCEPTION, ErrorMsg.EXCEPTION.getMsg());
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public <T> ResponseVo<T> bizExceptionHandler(BusinessException e) {
        log.warn("A biz exception occurred on the request. Error message: " + e.getMsg());

        return ResponseVo.create(e.getCode(), e.getMsg());
    }

    /**
     * 请求参数类型错误
     */
    @ExceptionHandler(TypeMismatchException.class)
    public <T> ResponseVo<T> typeMismatchExceptionHandler(TypeMismatchException e) {
        log.warn("Incorrect request parameter type.", e);
        return ResponseVo.create(RespCodeEnum.PARAM_TYPE_ERROR, ErrorMsg.PARAM_TYPE_ERROR.getMsg());
    }

    /**
     * 请求参数类型错误
     */
    @ExceptionHandler(BindException.class)
    public <T> ResponseVo<T> bindExceptionExceptionHandler(BindException e) {
        log.warn("Incorrect request parameter type.", e);
        String errMsg = null;
        if (null != e.getBindingResult().getFieldError()) {
            errMsg = e.getBindingResult().getFieldError().getDefaultMessage();
        }
        return ResponseVo.create(RespCodeEnum.PARAM_TYPE_ERROR.getCode(), errMsg == null ? ErrorMsg.PARAM_TYPE_ERROR.getMsg() : errMsg);
    }

    /**
     * 请求方法不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public <T> ResponseVo<T> httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException e) {
        log.warn("Request method not supported.", e);
        return ResponseVo.create(RespCodeEnum.REQUEST_METHOD_ERROR, ErrorMsg.REQUEST_METHOD_ERROR.getMsg());
    }

    /**
     * 请求参数格式错误
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public <T> ResponseVo<T> httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException e) {
        log.warn("Incorrect format of request parameters.", e);
        return ResponseVo.create(RespCodeEnum.PARAM_FORMAT_ERROR, ErrorMsg.PARAM_FORMAT_ERROR.getMsg());
    }

    /**
     * 请求参数校验失败
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public <T> ResponseVo<T> validateExceptionHandler(MethodArgumentNotValidException e) {
        log.warn("Request parameters validation failed.", e);
        String errMsg = null;
        if (null != e.getBindingResult().getFieldError()) {
            errMsg = e.getBindingResult().getFieldError().getDefaultMessage();
        }
        return ResponseVo.create(RespCodeEnum.PARAM_ERROR.getCode(), errMsg == null ? ErrorMsg.PARAM_ERROR.getMsg() : errMsg);
    }

    /**
     * 上传文件大小超出限制
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public <T> ResponseVo<T> httpMessageNotReadableExceptionHandler(MaxUploadSizeExceededException e) {
        log.warn("File size out of limit.", e);
        return ResponseVo.create(RespCodeEnum.PARAM_ERROR.getCode(), ErrorMsg.PARAM_ERROR.getMsg() + FILE_SIZE_OUT_LIMIT);
    }
}

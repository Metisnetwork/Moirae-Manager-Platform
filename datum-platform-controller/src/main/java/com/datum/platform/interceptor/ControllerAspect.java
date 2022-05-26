package com.datum.platform.interceptor;

import com.datum.platform.common.enums.ErrorMsg;
import com.datum.platform.common.enums.RespCodeEnum;
import com.datum.platform.common.exception.BusinessException;
import com.datum.platform.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * @author admin
 * @date 2021/7/20
 */
@Slf4j
@Aspect
@Component
public class ControllerAspect {

    @Pointcut("execution(* com.datum.platform.controller..*.*(..))")
    public void pointCut() {

    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint pjp) {
        long start = System.currentTimeMillis();
        // 被拦截的类
        String clazzName = pjp.getTarget().getClass().getName();
        // 被拦截的方法签名
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        // 被拦截的方法名
        String methodName = methodSignature.getName();
        // 请求参数
        Object[] args = pjp.getArgs();

        log.info("Request {}.{} start... ...", clazzName, methodName);
        if (null != args && args.length > 0) {
            log.info("Request parameters: {}.", args[0]);
        }

        Object returnObj;
        try {
            returnObj = pjp.proceed(args);
        } catch (BusinessException e) {
            log.warn("A biz exception occurred on the request. Error message: " + e.getMsg());

            returnObj = ResponseVo.create(e.getCode(), e.getMsg());
        } catch (Throwable e) {
            log.error("An exception occurred on the request.", e);

            returnObj = ResponseVo.create(RespCodeEnum.EXCEPTION, ErrorMsg.EXCEPTION.getMsg());
        }

        log.info("End of request, Used Time: {}ms, Return result: {}.", (System.currentTimeMillis() - start), returnObj);
        return returnObj;
    }
}

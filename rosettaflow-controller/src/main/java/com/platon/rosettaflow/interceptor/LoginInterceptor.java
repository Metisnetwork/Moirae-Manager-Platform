package com.platon.rosettaflow.interceptor;

import cn.hutool.core.util.StrUtil;
import com.platon.rosettaflow.common.constants.SysConstant;
import com.platon.rosettaflow.dto.UserDto;
import com.platon.rosettaflow.service.ITokenService;
import com.platon.rosettaflow.service.utils.UserContext;
import com.platon.rosettaflow.utils.IpUtil;
import com.platon.rosettaflow.common.utils.LanguageContext;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author admin
 * @date 2021/7/20
 */
@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    private static final String ENCODING = "UTF-8";
    private static final String CONTENT_TYPE = "application/json;charset=utf-8";

    @Resource
    private ITokenService tokenService;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        // 加入日志跟踪号
        addRequestId();

        LanguageContext.set(request.getHeader("Accept-Language"));

        log.info("Request Info: [Method = {}], [URI = {}], [Client-IP = {}], [userAgent = {}]", request.getMethod(),
                request.getRequestURI(), IpUtil.getIpAddr(request), request.getHeader("user-agent"));

        String token = request.getHeader(SysConstant.HEADER_TOKEN_KEY);
        UserDto userDto;
        if (StrUtil.isNotEmpty(token)) {
            userDto = tokenService.getUserByToken(token);
            if (null != userDto) {
                UserContext.set(userDto);
                tokenService.refreshToken(token);
            } else {
                log.warn("Invalid token: {}", token);
            }
        }
        return true;
    }

    @Override
    public void postHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, ModelAndView modelAndView) {
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                @NonNull Object handler, Exception ex) {
        removeRequestId();
        UserContext.remove();
        LanguageContext.remove();
    }

    void addRequestId() {
        MDC.put("requestId", UUID.randomUUID().toString().replaceAll("-", "").toLowerCase());
    }

    void removeRequestId() {
        MDC.clear();
    }

}

package com.moirae.rosettaflow.service.utils;

import com.moirae.rosettaflow.common.enums.ErrorMsg;
import com.moirae.rosettaflow.common.enums.RespCodeEnum;
import com.moirae.rosettaflow.common.exception.BusinessException;
import com.moirae.rosettaflow.dto.UserDto;

/**
 * @author admin
 * @date 2021/7/20
 */
public class UserContext {
    private static final ThreadLocal<UserDto> USER_THREAD_LOCAL = new ThreadLocal<>();

    private UserContext() {
    }

    public static UserDto get() {
        return USER_THREAD_LOCAL.get();
    }

    public static void set(UserDto userDto) {
        USER_THREAD_LOCAL.remove();
        USER_THREAD_LOCAL.set(userDto);
    }

    public static void remove() {
        USER_THREAD_LOCAL.remove();
    }

    public static UserDto getCurrentUser() {
        UserDto currentUser = UserContext.get();
        if (null == currentUser) {
            throw new BusinessException(RespCodeEnum.UN_LOGIN, ErrorMsg.UN_LOGIN.getMsg());
        }
        return currentUser;
    }

    public static UserDto getCurrentUserOrNull() {
        return UserContext.get();
    }
}

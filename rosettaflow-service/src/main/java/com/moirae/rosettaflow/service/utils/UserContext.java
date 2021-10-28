package com.moirae.rosettaflow.service.utils;

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
}

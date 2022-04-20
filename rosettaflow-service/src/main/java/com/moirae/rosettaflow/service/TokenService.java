package com.moirae.rosettaflow.service;

import com.moirae.rosettaflow.dto.UserDto;

import javax.validation.constraints.NotNull;

/**
 * @author admin
 * @date 2021/7/20
 */
public interface TokenService {
    /**
     * 设置token
     *
     * @param userDto 用户讲求对象
     * @return token
     */
    String setToken(@NotNull UserDto userDto);

    /**
     * 根据token获取用户信息
     *
     * @param token token参数
     * @return UserDto
     */
    UserDto getUserByToken(@NotNull String token);

    /**
     * 删除token
     *
     * @param token token
     */
    void removeToken(String token);

    /**
     * 刷新token
     *
     * @param token token
     * @return boolean
     */
    boolean refreshToken(String token);
}

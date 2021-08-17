package com.platon.rosettaflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.platon.rosettaflow.dto.UserDto;
import com.platon.rosettaflow.mapper.domain.User;

/**
 * @author admin
 * @date 2021/8/16
 */
public interface IUserService extends IService<User> {

    /**
     * 根据地址获取用户信息
     * @param address 用户地址
     * @return 用户信息
     */
    User getByAddress(String address);

    /**
     * 生成用户token
     * @param address 用户钱包地址
     * @return 用户信息
     */
    UserDto generatorToken(String address);
}

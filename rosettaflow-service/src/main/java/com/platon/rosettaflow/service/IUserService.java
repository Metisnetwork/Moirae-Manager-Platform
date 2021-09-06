package com.platon.rosettaflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.platon.rosettaflow.dto.UserDto;
import com.platon.rosettaflow.mapper.domain.User;

import java.util.List;
import java.util.Map;

/**
 * @author admin
 * @date 2021/8/16
 */
public interface IUserService extends IService<User> {

    /**
     * 根据地址获取用户信息
     *
     * @param address 用户地址
     * @return 用户信息
     */
    User getByAddress(String address);

    /**
     * 生成用户token
     *
     * @param address  用户钱包地址
     * @param userType 用户类型
     * @return 用户信息
     */
    UserDto generatorToken(String address, Byte userType);

    /**
     * 登出
     *
     * @param address 钱包地址
     */
    void logout(String address);

    /**
     * 修改昵称
     *
     * @param address  钱包地址
     * @param nickName 昵称
     */
    void updateNickName(String address, String nickName);

    /**
     * 查询所有用户昵称
     *
     * @return 所有用户昵称
     */
    List<Map<String, Object>> queryAllUserNickName();

    /**
     * 获取登录nonce
     *
     * @param address 用户钱包地址
     * @return nonce
     */
    String getLoginNonce(String address);

    /**
     * 检查nonce有效性
     *
     * @param nonce   nonce
     * @param address 用户地址
     */
    void checkNonceValidity(String nonce, String address);
}

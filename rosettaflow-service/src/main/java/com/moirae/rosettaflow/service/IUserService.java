package com.moirae.rosettaflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.dto.UserDto;
import com.moirae.rosettaflow.mapper.domain.User;

import java.util.List;

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
     * @param address    用户钱包地址0x
     * @param hrpAddress hrp钱包地址
     * @return 用户信息
     */
    UserDto generatorToken(String address, String hrpAddress);

    /**
     * 登出
     */
    void logout();

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
     * @return User
     */
    List<User> queryAllUserNickName();

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

    /**
     * 根据项目id查询所有可以使用的用户信息
     *
     * @param projectId 项目id
     * @return 用户列表
     */
    List<User> queryUserByProjectId(Long projectId);
}

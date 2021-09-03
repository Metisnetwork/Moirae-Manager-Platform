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
     * @param address 用户地址
     * @return 用户信息
     */
    User getByAddress(String address);

    /**
     * 生成用户token
     * @param address 用户钱包地址
     * @param userType 用户类型
     * @return 用户信息
     */
    UserDto generatorToken(String address,Byte userType);

    /**
     * 登出
     * @param address 钱包地址
     */
    void logout(String address);

    /**
     * 修改昵称
     * @param address 钱包地址
     * @param nickName 昵称
     */
    void updateNickName(String address, String nickName);

    /**
     * 查询所有用户昵称
     */
    List<Map<String, Object>> queryAllUserNickname();

    /**
     * 获取登录nonce
     * @return 返回uuid
     */
    String getLoginNonce();

    /**
     * 检查nonce有效性
     * @return  true：nonce有效 、false:nonce无效
     */
    boolean checkNonceValidity(String nonce);
}

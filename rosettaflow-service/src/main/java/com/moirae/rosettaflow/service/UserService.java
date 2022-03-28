package com.moirae.rosettaflow.service;

import com.moirae.rosettaflow.dto.UserDto;
import com.moirae.rosettaflow.mapper.domain.User;

import java.util.List;

public interface UserService {


    /**
     * 根据地址获取用户信息
     *
     * @param address 用户地址
     * @return 用户信息
     */
    User getByAddress(String address);

    /**
     * 通过签名方式登录
     *
     * @param hexAddress
     * @param hrpAddress
     * @param authenticateSignMessage
     * @param authenticateSign
     * @return
     */
    UserDto loginBySign(String hexAddress, String hrpAddress, String authenticateSignMessage, String authenticateSign);

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
}

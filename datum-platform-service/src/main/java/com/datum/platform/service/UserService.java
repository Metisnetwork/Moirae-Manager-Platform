package com.datum.platform.service;

import com.datum.platform.dto.UserDto;
import com.datum.platform.mapper.domain.User;
import com.datum.platform.service.dto.user.NonceDto;
import com.datum.platform.service.dto.user.UserAddressDto;

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
     * @param nickName 昵称
     */
    void updateUserName(String nickName);

    /**
     * 获取登录nonce
     *
     * @param address 用户钱包地址
     * @return nonce
     */
    NonceDto getLoginNonce(UserAddressDto address);

    /**
     * 更新用户心跳时间
     *
     * @return
     */
    boolean updateHeartBeat(String address);

    /**
     * 查询所有在线用户地址
     *
     * @return
     */
    List<String> getOnlineUserIdList();

    int countOfActiveAddress();
}

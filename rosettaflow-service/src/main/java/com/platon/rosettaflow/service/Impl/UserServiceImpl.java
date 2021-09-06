package com.platon.rosettaflow.service.Impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.platon.rosettaflow.common.constants.SysConfig;
import com.platon.rosettaflow.common.constants.SysConstant;
import com.platon.rosettaflow.common.enums.ErrorMsg;
import com.platon.rosettaflow.common.enums.RespCodeEnum;
import com.platon.rosettaflow.common.exception.BusinessException;
import com.platon.rosettaflow.common.utils.RedisUtil;
import com.platon.rosettaflow.dto.SignMessageDto;
import com.platon.rosettaflow.dto.UserDto;
import com.platon.rosettaflow.mapper.UserMapper;
import com.platon.rosettaflow.mapper.domain.User;
import com.platon.rosettaflow.service.CommonService;
import com.platon.rosettaflow.service.ITokenService;
import com.platon.rosettaflow.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author admin
 * @date 2021/8/16
 * @description 用户服务实现类
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private ITokenService tokenService;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private SysConfig sysConfig;

    @Resource
    private CommonService commonService;

    @Override
    public User getByAddress(String address) {
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(User::getAddress, address);
        return this.getOne(wrapper);
    }

    @Override
    public UserDto generatorToken(String address, Byte userType) {
        User user = this.getByAddress(address);
        if (user == null) {
            user = new User();
            // 用户昵称
            user.setUserName(address.substring(0, 12) + address.substring(address.length() - 4));
            // 钱包地址
            user.setAddress(address);
            //用户类型
            user.setUserType(userType);
            this.save(user);
        }
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        userDto.setToken(tokenService.setToken(userDto));
        return userDto;
    }

    @Override
    public void logout(String address) {
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(User::getAddress, address);
        User user = this.getOne(wrapper);
        // 获取token的key
        String key = TokenServiceImpl.getTokenKey(user.getId());
        // 获取token
        String token = (String) redisUtil.get(key);
        // 删除缓存中的token
        redisUtil.delete(key);
        // 删除缓存中的用户信息
        redisUtil.delete(TokenServiceImpl.getUserKey(token));
    }

    @Override
    public void updateNickName(String address, String nickName) {
        LambdaUpdateWrapper<User> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(User::getAddress, address);
        updateWrapper.set(User::getUserName, nickName);
        this.update(updateWrapper);
    }

    @Override
    public List<Map<String, Object>> queryAllUserNickName() {
        return userMapper.queryAllUserNickname();
    }

    @Override
    public String getLoginNonce(String address) {
        String nonce = commonService.generateUuid();
        redisUtil.set(StrUtil.format(SysConstant.REDIS_USER_NONCE_KEY, address, nonce), nonce, sysConfig.getNonceTimeOut());
        return nonce;
    }

    @Override
    public boolean checkNonceValidity(String signMessage, String address) {

        SignMessageDto signMessageDto;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            signMessageDto = objectMapper.readValue(signMessage, SignMessageDto.class);
        } catch (Exception e) {
            throw new BusinessException(RespCodeEnum.PARAM_ERROR, ErrorMsg.PARAM_ERROR.getMsg());
        }
        if(Objects.isNull(signMessageDto.getMessage()) || !StrUtil.isNotEmpty(signMessageDto.getMessage().getKey())){
            throw new BusinessException(RespCodeEnum.PARAM_ERROR, ErrorMsg.PARAM_ERROR.getMsg());
        }
        String nonce = signMessageDto.getMessage().getKey();
        if (StrUtil.isEmpty(nonce)) {
            throw new BusinessException(RespCodeEnum.PARAM_ERROR, ErrorMsg.PARAM_ERROR.getMsg());
        }

        String redisKey = StrUtil.format(SysConstant.REDIS_USER_NONCE_KEY, address, nonce);

        if (!redisUtil.hasKey(redisKey)) {
            return false;
        }

        return redisUtil.expire(redisKey, 1);
    }
}

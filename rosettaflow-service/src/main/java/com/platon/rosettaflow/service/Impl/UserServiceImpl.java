package com.platon.rosettaflow.service.Impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.platon.rosettaflow.common.constants.SysConfig;
import com.platon.rosettaflow.common.constants.SysConstant;
import com.platon.rosettaflow.common.enums.ErrorMsg;
import com.platon.rosettaflow.common.enums.RespCodeEnum;
import com.platon.rosettaflow.common.exception.BusinessException;
import com.platon.rosettaflow.dto.SignMessageDto;
import com.platon.rosettaflow.dto.UserDto;
import com.platon.rosettaflow.mapper.UserMapper;
import com.platon.rosettaflow.mapper.domain.User;
import com.platon.rosettaflow.service.ITokenService;
import com.platon.rosettaflow.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private SysConfig sysConfig;

    @Override
    public User getByAddress(String address) {
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(User::getAddress, address);
        return this.getOne(wrapper);
    }

    @Override
    public UserDto generatorToken(String address,Byte userType) {
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
        String token = (String) redisTemplate.opsForValue().get(key);
        // 删除缓存中的token
        redisTemplate.delete(key);
        // 删除缓存中的用户信息
        redisTemplate.delete(TokenServiceImpl.getUserKey(token));
    }

    @Override
    public void updateNickName(String address, String nickName) {
        LambdaUpdateWrapper<User> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(User::getAddress, address);
        updateWrapper.set(User::getUserName, nickName);
        this.update(updateWrapper);
    }

    @Override
    public List<Map<String, Object>> queryAllUserNickname() {
        return userMapper.queryAllUserNickname();
    }

    @Override
    public String getLoginNonce() {
        String nonce = generateNonce();
        redisTemplate.opsForValue().set(nonce, getNonceValue(nonce), sysConfig.getNonceTimeOut(), TimeUnit.MILLISECONDS);
        return nonce;
    }

    @Override
    public boolean checkNonceValidity(String signMessage) {

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
        boolean isExistKey = Boolean.TRUE.equals(redisTemplate.hasKey(nonce));
        if(!isExistKey){
            return false;
        }
        String value = (String) redisTemplate.opsForValue().get(nonce);
        if(!StrUtil.equals(value, getNonceValue(nonce))){
            return false;
        }
        redisTemplate.delete(nonce);
        return true;
    }


    public static String generateNonce() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

    public static String getNonceValue(String nonce) {
        return SysConstant.REDIS_USER_PREFIX_KEY + nonce;
    }

}

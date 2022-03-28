package com.moirae.rosettaflow.service.impl;

import cn.hutool.core.util.StrUtil;
import com.moirae.rosettaflow.common.constants.SysConfig;
import com.moirae.rosettaflow.common.constants.SysConstant;
import com.moirae.rosettaflow.dto.UserDto;
import com.moirae.rosettaflow.service.ITokenService;
import com.zengtengpeng.operation.RedissonObject;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author admin
 * @date 2021/7/20
 */
@Slf4j
@Service
public class TokenServiceImpl implements ITokenService {

    @Resource
    private SysConfig sysConfig;

    @Resource
    private RedissonObject redissonObject;

    @Resource
    private RedissonClient redissonClient;

    public static String getTokenKey(String id) {
        return SysConstant.REDIS_TOKEN_PREFIX_KEY + id;
    }

    public static String getUserKey(String token) {
        return SysConstant.REDIS_USER_PREFIX_KEY + token;
    }

    public static String generateToken() {
        String uuid = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        return System.currentTimeMillis() + uuid;
    }

    @Override
    public String setToken(@NotNull UserDto userDto) {
        String key = getTokenKey(userDto.getAddress());
        String token = redissonObject.getValue(key);
        if (StrUtil.isNotEmpty(token)) {
            if (sysConfig.isKickMode()) {
                redissonObject.delete(key);
                redissonObject.delete(getUserKey(token));
                token = generateToken();
            }
        } else {
            token = generateToken();
        }
        if (!sysConfig.isKickMode()) {
            //记录此token登录的设备数
            log.info("current user:{},login in {} device", userDto.getUserName(), redissonClient.getAtomicLong(SysConstant.REDIS_TOKEN_BIND_PREFIX_KEY + token).incrementAndGet());
        }
        saveTokenToRedis(userDto, key, token);
        return token;
    }

    private void saveTokenToRedis(@NotNull UserDto userDto, String key, String token) {
        userDto.setToken(token);
        redissonObject.setValue(key, token, sysConfig.getLoginTimeOut());

        String userKey = getUserKey(token);
        redissonObject.setValue(userKey, userDto, sysConfig.getLoginTimeOut());
    }

    @Override
    public String getToken(@NotNull UserDto userDto) {
        String key = getTokenKey(userDto.getAddress());
        String token = redissonObject.getValue(key);
        if (StrUtil.isNotEmpty(token)) {
            return token;
        }
        return null;
    }

    @Override
    public UserDto getUserByToken(@NotNull String token) {
        UserDto userDto = redissonObject.getValue(getUserKey(token));
        if (userDto == null) {
            log.warn("Can not find user by token: {}.", token);
        }
        return userDto;
    }

    @Override
    public void removeToken(@NotNull String token) {
        String userKey = getUserKey(token);
        UserDto userDto = redissonObject.getValue(userKey);
        if (userDto != null) {
            //判断是否有多个设备登录，如果只有一台直接清redis，否则减少登录设备记录数
            if (!sysConfig.isKickMode()) {
                Object loginDeviceNum = redissonObject.getValue(SysConstant.REDIS_TOKEN_BIND_PREFIX_KEY + token);
                if (null != loginDeviceNum && (Integer) loginDeviceNum > 1) {
                    log.info("current user:{},login in {} device", userDto.getUserName(), redissonClient.getAtomicLong(SysConstant.REDIS_TOKEN_BIND_PREFIX_KEY + token).decrementAndGet());
                } else {
                    String tokeKey = getTokenKey(userDto.getAddress());
                    redissonObject.delete(tokeKey);
                    redissonObject.delete(userKey);
                }
            } else {
                String tokeKey = getTokenKey(userDto.getAddress());
                redissonObject.delete(tokeKey);
                redissonObject.delete(userKey);
            }

        }
    }

    @Override
    public boolean refreshToken(@NotNull String token) {
        String userKey = getUserKey(token);
        UserDto userDto = redissonObject.getValue(userKey);
        if (userDto != null) {
            String tokeKey = getTokenKey(userDto.getAddress());
            redissonObject.getBucket(tokeKey).expire(sysConfig.getLoginTimeOut(), TimeUnit.MILLISECONDS);
            redissonObject.getBucket(userKey).expire(sysConfig.getLoginTimeOut(), TimeUnit.MILLISECONDS);
        }
        return true;
    }
}

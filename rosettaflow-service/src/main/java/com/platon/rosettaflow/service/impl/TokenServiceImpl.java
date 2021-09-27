package com.platon.rosettaflow.service.impl;

import cn.hutool.core.util.StrUtil;
import com.platon.rosettaflow.common.constants.SysConfig;
import com.platon.rosettaflow.common.constants.SysConstant;
import com.platon.rosettaflow.dto.UserDto;
import com.platon.rosettaflow.service.ITokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
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
    private RedisTemplate<String, Object> redisTemplate;

    public static String getTokenKey(Long id) {
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
        String key = getTokenKey(userDto.getId());
        String token = (String) redisTemplate.opsForValue().get(key);
        if (StrUtil.isNotEmpty(token)) {
            if (sysConfig.isKickMode()) {
                redisTemplate.delete(key);
                redisTemplate.delete(getUserKey(token));
                token = generateToken();
            }
        } else {
            token = generateToken();
        }
        if (!sysConfig.isKickMode()) {
            //记录此token登录的设备数
            redisTemplate.opsForValue().increment(SysConstant.REDIS_TOKEN_BIND_PREFIX_KEY + token);
            log.info("current user:{},login in {} device", userDto.getUserName(), redisTemplate.opsForValue().get(SysConstant.REDIS_TOKEN_BIND_PREFIX_KEY + token));
        }
        saveTokenToRedis(userDto, key, token);
        return token;
    }

    private void saveTokenToRedis(@NotNull UserDto userDto, String key, String token) {
        userDto.setToken(token);
        redisTemplate.opsForValue().set(key, token, sysConfig.getLoginTimeOut(), TimeUnit.MILLISECONDS);

        String userKey = getUserKey(token);
        redisTemplate.opsForValue().set(userKey, userDto, sysConfig.getLoginTimeOut(), TimeUnit.MILLISECONDS);
    }

    @Override
    public String getToken(@NotNull UserDto userDto) {
        String key = getTokenKey(userDto.getId());
        String token = (String) redisTemplate.opsForValue().get(key);
        if (StrUtil.isNotEmpty(token)) {
            return token;
        }
        return null;
    }

    @Override
    public UserDto getUserByToken(@NotNull String token) {
        UserDto userDto = (UserDto) redisTemplate.opsForValue().get(getUserKey(token));
        if (userDto == null) {
            log.warn("Can not find user by token: {}.", token);
        }
        return userDto;
    }

    @Override
    public void removeToken(@NotNull String token) {
        String userKey = getUserKey(token);
        UserDto userDto = (UserDto) redisTemplate.opsForValue().get(userKey);
        if (userDto != null) {
            //判断是否有多个设备登录，如果只有一台直接清redis，否则减少登录设备记录数
            if (!sysConfig.isKickMode()) {
                Object loginDeviceNum = redisTemplate.opsForValue().get(SysConstant.REDIS_TOKEN_BIND_PREFIX_KEY + token);
                if (null != loginDeviceNum && (Integer) loginDeviceNum > 1) {
                    redisTemplate.opsForValue().decrement(SysConstant.REDIS_TOKEN_BIND_PREFIX_KEY + token);
                    log.info("current user:{},login in {} device", userDto.getUserName(), redisTemplate.opsForValue().get(SysConstant.REDIS_TOKEN_BIND_PREFIX_KEY + token));
                } else {
                    String tokeKey = getTokenKey(userDto.getId());
                    redisTemplate.delete(tokeKey);
                    redisTemplate.delete(userKey);
                }
            } else {
                String tokeKey = getTokenKey(userDto.getId());
                redisTemplate.delete(tokeKey);
                redisTemplate.delete(userKey);
            }

        }
    }

    @Override
    public boolean refreshToken(@NotNull String token) {
        String userKey = getUserKey(token);
        UserDto userDto = (UserDto) redisTemplate.opsForValue().get(userKey);
        if (userDto != null) {
            String tokeKey = getTokenKey(userDto.getId());
            redisTemplate.expire(tokeKey, sysConfig.getLoginTimeOut(), TimeUnit.MILLISECONDS);
            redisTemplate.expire(userKey, sysConfig.getLoginTimeOut(), TimeUnit.MILLISECONDS);
        }
        return true;
    }

    @Override
    public boolean removeTokenById(@NotNull Long id) {
        String tokenKey = getTokenKey(id);
        String token = (String) redisTemplate.opsForValue().get(tokenKey);
        if (token != null) {
            redisTemplate.delete(tokenKey);
            redisTemplate.delete(getUserKey(token));
        }
        return true;
    }

    @Override
    public boolean refreshUserDto(@NotNull UserDto userDto) {
        String token = getToken(userDto);
        if (StrUtil.isEmpty(token)) {
            log.warn("The token is not exists. UserId: {}.", userDto.getId());
            return false;
        } else {
            String userKey = getUserKey(token);
            redisTemplate.opsForValue().set(userKey, userDto, sysConfig.getLoginTimeOut(), TimeUnit.MILLISECONDS);
            return true;
        }
    }

}

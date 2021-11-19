package com.moirae.rosettaflow.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moirae.rosettaflow.common.constants.SysConfig;
import com.moirae.rosettaflow.common.constants.SysConstant;
import com.moirae.rosettaflow.common.enums.ErrorMsg;
import com.moirae.rosettaflow.common.enums.RespCodeEnum;
import com.moirae.rosettaflow.common.enums.StatusEnum;
import com.moirae.rosettaflow.common.exception.BusinessException;
import com.moirae.rosettaflow.dto.SignMessageDto;
import com.moirae.rosettaflow.dto.UserDto;
import com.moirae.rosettaflow.mapper.UserMapper;
import com.moirae.rosettaflow.mapper.domain.User;
import com.moirae.rosettaflow.service.CommonService;
import com.moirae.rosettaflow.service.ITokenService;
import com.moirae.rosettaflow.service.IUserService;
import com.zengtengpeng.operation.RedissonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * 用户服务实现类
 *
 * @author admin
 * @date 2021/8/16
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private ITokenService tokenService;

    @Resource
    private RedissonObject redissonObject;

    @Resource
    private SysConfig sysConfig;

    @Resource
    private CommonService commonService;

    @Override
    public User getByAddress(String address) {
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(User::getAddress, address);
        wrapper.eq(User::getStatus, StatusEnum.VALID.getValue());
        return this.getOne(wrapper);
    }

    @Override
    public UserDto generatorToken(String address, String hrpAddress) {
        User user = this.getByAddress(address);
        if (user == null) {
            user = new User();
            // 用户昵称
            user.setUserName(hrpAddress);
            // 钱包地址
            user.setAddress(address);
            this.save(user);
        }
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        userDto.setToken(tokenService.setToken(userDto));
        return userDto;
    }

    @Override
    public void logout() {
        try {
            UserDto userDto = commonService.getCurrentUser();
            tokenService.removeToken(userDto.getToken());
        } catch (BusinessException e) {
            log.info("User not login not need to logout");
        }
    }

    @Override
    public void updateNickName(String address, String nickName) {
        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(User::getUserName, nickName);
        queryWrapper.eq(User::getStatus, StatusEnum.VALID.getValue());
        User user = this.getOne(queryWrapper);
        if (!Objects.isNull(user)) {
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.USER_NAME_EXISTED.getMsg());
        }

        try {
            LambdaUpdateWrapper<User> updateWrapper = Wrappers.lambdaUpdate();
            updateWrapper.eq(User::getAddress, address);
            updateWrapper.eq(User::getStatus, StatusEnum.VALID.getValue());
            updateWrapper.set(User::getUserName, nickName);
            this.update(updateWrapper);
        } catch (Exception e) {
            log.error("updateNickName--修改用户昵称失败, 错误信息:{}", e.getMessage());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.MODIFY_USER_NAME_FAILED.getMsg());
        }
    }

    @Override
    public List<User> queryAllUserNickName() {
        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(User::getStatus, StatusEnum.VALID.getValue());
        return this.list(queryWrapper);
    }

    @Override
    public String getLoginNonce(String address) {
        String nonce = commonService.generateUuid();
        redissonObject.setValue(StrUtil.format(SysConstant.REDIS_USER_NONCE_KEY, address, nonce), nonce, sysConfig.getNonceTimeOut());
        return nonce;
    }

    @Override
    public void checkNonceValidity(String signMessage, String address) {

        SignMessageDto signMessageDto;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            signMessageDto = objectMapper.readValue(signMessage, SignMessageDto.class);
        } catch (Exception e) {
            log.error(ErrorMsg.PARAM_ERROR.getMsg(), e);
            throw new BusinessException(RespCodeEnum.PARAM_ERROR, ErrorMsg.PARAM_ERROR.getMsg());
        }
        if (Objects.isNull(signMessageDto.getMessage()) || StrUtil.isEmpty(signMessageDto.getMessage().getKey())) {
            log.error(ErrorMsg.PARAM_ERROR.getMsg());
            throw new BusinessException(RespCodeEnum.PARAM_ERROR, ErrorMsg.PARAM_ERROR.getMsg());
        }
        String nonce = signMessageDto.getMessage().getKey();
        if (StrUtil.isEmpty(nonce)) {
            log.error("Nonce is empty!");
            throw new BusinessException(RespCodeEnum.PARAM_ERROR, ErrorMsg.PARAM_ERROR.getMsg());
        }

        String redisKey = StrUtil.format(SysConstant.REDIS_USER_NONCE_KEY, address, nonce);

        if (!redissonObject.delete(redisKey)) {
            log.error("Delete old user nonce fail!");
            throw new BusinessException(RespCodeEnum.NONCE_INVALID, ErrorMsg.USER_NONCE_INVALID.getMsg());
        }
    }

    @Override
    public List<User> queryUserByProjectId(Long projectId) {
        return this.baseMapper.queryUserByProjectId(projectId);
    }
}
